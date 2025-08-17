package top.ticho.rainbow.application.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import top.ticho.rainbow.domain.entity.TaskLog;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.common.component.TaskTemplate;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.tool.core.TiExceptionUtil;
import top.ticho.tool.core.TiLocalDateTimeUtil;
import top.ticho.tool.core.TiStrUtil;
import top.ticho.tool.json.constant.TiDateFormatConst;
import top.ticho.tool.json.util.TiJsonUtil;
import top.ticho.trace.common.TiHttpTraceTag;
import top.ticho.trace.common.TiTraceConst;
import top.ticho.trace.common.TiTraceContext;
import top.ticho.trace.common.TiTraceProperty;
import top.ticho.trace.spring.util.TiIpUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@RequiredArgsConstructor
public abstract class AbstracTask<T> extends QuartzJobBean {
    private final Environment environment;
    private final TiTraceProperty tiTraceProperty;
    private final TaskLogRepository taskLogRepository;

    public abstract void run(JobExecutionContext context);

    public abstract Class<T> getParamClass();

    public T getTaskParam(String taskParam) {
        Class<T> paramClass = getParamClass();
        if (Objects.equals(String.class, paramClass)) {
            return paramClass.cast(taskParam);
        }
        return TiJsonUtil.toObject(taskParam, paramClass);
    }

    public T getTaskParam(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String taskParam = jobDataMap.getString(TaskTemplate.TASK_PARAM);
        if (TiStrUtil.isBlank(taskParam)) {
            return null;
        }
        T taskParamObj = getTaskParam(taskParam);
        TiAssert.isNotNull(taskParamObj, "任务参数异常");
        return taskParamObj;
    }

    public void executeInternal(JobExecutionContext context) {
        long start = System.currentTimeMillis();
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Date scheduledFireTime = context.getScheduledFireTime();
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        mdcHandle(jobDataMap);
        String taskName = jobDataMap.getString(TaskTemplate.TASK_NAME);
        String taskParam = jobDataMap.getString(TaskTemplate.TASK_PARAM);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TiDateFormatConst.YYYY_MM_DD_HH_MM_SS);
        String runTime = formatter.format(scheduledFireTime.toInstant());
        String jobClassName = jobDetail.getJobClass().getName();
        int isErr = 0;
        String errorMsg = null;
        try {
            log.info("定时任务开始, 任务:{}, 任务名称:{}, 任务时间:{}, 任务类:{}, 任务参数:{}", jobKey, taskName, runTime, jobClassName, taskParam);
            run(context);
        } catch (Exception e) {
            log.error("定时任务异常, 任务:{}, 任务名称:{}, 任务时间:{}, 任务类:{}, 异常信息:{}", jobKey, taskName, runTime, jobClassName, e.getMessage(), e);
            isErr = 1;
            errorMsg = TiExceptionUtil.stacktraceToString(e);
        } finally {
            long end = System.currentTimeMillis();
            long consume = end - start;
            log.info("定时任务结束, 任务:{}, 任务名称:{}, 耗时{}ms, 任务时间:{}, 任务类:{}", jobKey, taskName, consume, runTime, jobClassName);
            saveTaskLog(jobKey.getName(), jobClassName, taskParam, scheduledFireTime, start, end, consume, isErr, errorMsg);
            traceHandle();
        }
    }

    private void saveTaskLog(String jobName, String jobClassName, String taskParam, Date executeDate, long start, long end, long consume, int isErr, String errorMsg) {
        Map<String, String> mdcMap = MDC.getCopyOfContextMap();
        LocalDateTime executeTime = Optional.ofNullable(executeDate)
            .map(TiLocalDateTimeUtil::toLocalDateTime)
            .orElse(LocalDateTime.now());
        String username = mdcMap.get(CommConst.USERNAME_KEY);
        TaskLog taskLogPO = TaskLog.builder()
            .taskId(Long.parseLong(jobName))
            .content(jobClassName)
            .param(taskParam)
            .executeTime(executeTime)
            .startTime(TiLocalDateTimeUtil.of(start))
            .endTime(TiLocalDateTimeUtil.of(end))
            .consume(Long.valueOf(consume).intValue())
            .mdc(TiJsonUtil.toJsonString(mdcMap))
            .traceId(TiTraceContext.getTraceId())
            .status(Objects.equals(isErr, 1) ? 0 : 1)
            .operateBy(username)
            .isErr(isErr)
            .errMessage(errorMsg)
            .build();
        taskLogRepository.save(taskLogPO);
    }

    /**
     * 链路处理
     */
    private void traceHandle() {
        TiTraceContext.close();
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
        boolean hasTraceInfo = mdcMap.containsKey(TiTraceConst.TRACE_KEY);
        if (hasTraceInfo) {
            // mdc参数中本来就有username
            MDC.setContextMap(mdcMap);
            return;
        }
        String appName = environment.getProperty("spring.application.name");
        String traceId = mdcMap.get(TiTraceConst.TRACE_ID_KEY);
        TiTraceContext.start(appName, traceId, null, tiTraceProperty.getTrace());
        TiTraceContext.addTag(TiHttpTraceTag.IP, TiIpUtil.localIp());
    }


}
