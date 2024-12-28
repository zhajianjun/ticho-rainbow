package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import top.ticho.boot.view.util.TiAssert;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.core.constant.CommConst;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.rainbow.infrastructure.entity.TaskLog;
import top.ticho.tool.json.util.JsonUtil;
import top.ticho.tool.trace.common.bean.TraceInfo;
import top.ticho.tool.trace.common.constant.LogConst;
import top.ticho.tool.trace.common.prop.TraceProperty;
import top.ticho.tool.trace.core.handle.TracePushContext;
import top.ticho.tool.trace.core.util.TraceUtil;
import top.ticho.tool.trace.spring.event.TraceEvent;
import top.ticho.tool.trace.spring.util.IpUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zhajianjun
 * @date 2024-03-23 22:04
 */
@Slf4j
public abstract class AbstracTask<T> extends QuartzJobBean {

    @Resource
    private Environment environment;

    @Resource
    private TraceProperty traceProperty;

    @Resource
    private TaskLogRepository taskLogRepository;

    public abstract void run(JobExecutionContext context);

    public abstract Class<T> getParamClass();

    public T getTaskParam(String taskParam) {
        Class<T> paramClass = getParamClass();
        if (Objects.equals(String.class, paramClass)) {
            return paramClass.cast(taskParam);
        }
        return JsonUtil.toJavaObject(taskParam, paramClass);
    }

    public T getTaskParam(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String taskParam = jobDataMap.getString(TaskTemplate.TASK_PARAM);
        if (StrUtil.isBlank(taskParam)) {
            return null;
        }
        T taskParamObj = getTaskParam(taskParam);
        TiAssert.isNotNull(taskParamObj, "任务参数异常");
        return taskParamObj;
    }

    public void executeInternal(JobExecutionContext context) {
        long start = SystemClock.now();
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Date scheduledFireTime = context.getScheduledFireTime();
        JobDetail jobDetail = context.getJobDetail();
        mdcHandle(jobDataMap);
        String taskName = jobDataMap.getString(TaskTemplate.TASK_NAME);
        String taskParam = jobDataMap.getString(TaskTemplate.TASK_PARAM);
        String runTime = DateUtil.format(scheduledFireTime, DatePattern.NORM_DATETIME_FORMAT);
        String jobName = jobDetail.getKey().getName();
        String jobClassName = jobDetail.getJobClass().getName();
        int isErr = 0;
        String errorMsg = null;
        try {
            log.info("定时任务开始, 任务ID:{}, 任务名称:{}, 任务时间:{}, 任务类:{}, 任务参数:{}", jobName, taskName, runTime, jobClassName, taskParam);
            run(context);
        } catch (Exception e) {
            log.error("定时任务异常, 任务ID:{}, 任务名称:{}, 任务时间:{}, 任务类:{}, 异常信息:{}", jobName, taskName, runTime, jobClassName, e.getMessage(), e);
            isErr = 1;
            errorMsg = ExceptionUtil.stacktraceToString(e);
        } finally {
            long end = SystemClock.now();
            long consume = end - start;
            log.info("定时任务结束, 任务ID:{}, 任务名称:{}, 耗时{}ms, 任务时间:{}, 任务类:{}", jobName, taskName, consume, runTime, jobClassName);
            saveTaskLog(jobName, jobClassName, taskParam, scheduledFireTime, start, end, consume, isErr, errorMsg);
            traceHandle(jobDataMap, start, end, consume);
        }
    }

    private void saveTaskLog(String jobName, String jobClassName, String taskParam, Date executeDate, long start, long end, long consume, int isErr, String errorMsg) {
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        LocalDateTime executeTime = Optional.ofNullable(executeDate)
            .map(DateUtil::toLocalDateTime)
            .orElse(LocalDateTime.now());
        String username = mdcMap.get(CommConst.USERNAME_KEY);
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(Long.parseLong(jobName));
        taskLog.setContent(jobClassName);
        taskLog.setParam(taskParam);
        taskLog.setExecuteTime(executeTime);
        taskLog.setStartTime(LocalDateTimeUtil.of(start));
        taskLog.setEndTime(LocalDateTimeUtil.of(end));
        taskLog.setConsume(Long.valueOf(consume).intValue());
        taskLog.setMdc(JsonUtil.toJsonString(mdcMap));
        taskLog.setTraceId(mdcMap.get(LogConst.TRACE_ID_KEY));
        taskLog.setStatus(Objects.equals(isErr, 1) ? 0 : 1);
        taskLog.setOperateBy(username);
        taskLog.setIsErr(isErr);
        taskLog.setErrMessage(errorMsg);
        taskLogRepository.save(taskLog);
    }

    /**
     * 链路处理
     */
    private void traceHandle(JobDataMap mergedJobDataMap, long start, long end, long consume) {
        if (!mergedJobDataMap.containsKey(TaskTemplate.TASK_MDC_INFO)) {
            TraceUtil.complete();
            return;
        }
        TraceInfo traceInfo = TraceInfo.builder()
            .traceId(MDC.get(LogConst.TRACE_ID_KEY))
            .spanId(MDC.get(LogConst.SPAN_ID_KEY))
            .appName(MDC.get(LogConst.APP_NAME_KEY))
            .env(environment.getProperty("spring.profiles.active"))
            .ip(MDC.get(LogConst.IP_KEY))
            .preAppName(MDC.get(LogConst.PRE_APP_NAME_KEY))
            .preIp(MDC.get(LogConst.PRE_IP_KEY))
            // .url(url)
            // .port(port)
            // .method(handlerMethod.toString())
            // .type(type)
            // .status(status)
            .start(start)
            .end(end)
            .consume(consume)
            .build();
        TracePushContext.asyncPushTrace(traceProperty, traceInfo);
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        applicationContext.publishEvent(new TraceEvent(applicationContext, traceInfo));
        TraceUtil.complete();
    }

    /**
     * mdc处理
     */
    @SuppressWarnings("unchecked")
    private void mdcHandle(JobDataMap mergedJobDataMap) {
        Map<String, String> mdcMap;
        if (mergedJobDataMap.containsKey(TaskTemplate.TASK_MDC_INFO)) {
            Object mdcInfo = mergedJobDataMap.get(TaskTemplate.TASK_MDC_INFO);
            mdcMap = (Map<String, String>) mdcInfo;
        } else {
            mdcMap = new HashMap<>();
        }
        boolean hasTraceInfo = mdcMap.containsKey(LogConst.TRACE_KEY);
        if (hasTraceInfo) {
            // mdc参数中本来就有username
            MDC.setContextMap(mdcMap);
            UserUtil.userTrace(null);
            return;
        }
        String appName = environment.getProperty("spring.application.name");
        String ip = IpUtil.localIp();
        mdcMap.put(LogConst.APP_NAME_KEY, appName);
        mdcMap.put(LogConst.IP_KEY, ip);
        TraceUtil.prepare(mdcMap);
        UserUtil.userTrace("自动定时任务");
    }


}
