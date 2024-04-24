package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
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
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.tool.trace.common.bean.TraceInfo;
import top.ticho.tool.trace.common.constant.LogConst;
import top.ticho.tool.trace.common.prop.TraceProperty;
import top.ticho.tool.trace.core.handle.TracePushContext;
import top.ticho.tool.trace.core.util.TraceUtil;
import top.ticho.tool.trace.spring.event.TraceEvent;
import top.ticho.tool.trace.spring.util.IpUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    public abstract void run(JobExecutionContext context);

    public void before(JobExecutionContext context) {

    }

    public void complete(JobExecutionContext context) {

    }

    public T getTaskParam(JobExecutionContext context, Class<T> claz) {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        String taskParam = jobDataMap.getString(TaskTemplate.TASK_PARAM);
        if (StrUtil.isBlank(taskParam)) {
            return null;
        }
        return JsonUtil.toJavaObject(taskParam, claz);
    }

    public void executeInternal(JobExecutionContext context) {
        // @formatter:off
        long start = SystemClock.now();
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        mdcHandle(jobDataMap);
        String taskName = jobDataMap.getString(TaskTemplate.TASK_NAME);
        String taskParam = jobDataMap.getString(TaskTemplate.TASK_PARAM);
        String runTime = DateUtil.format(context.getScheduledFireTime(), DatePattern.NORM_DATETIME_FORMAT);
        JobDetail jobDetail = context.getJobDetail();
        String jobName = jobDetail.getKey().getName();
        String jobClassName = jobDetail.getJobClass().getName();
        try {
            log.info("定时任务开始, 任务ID:{}, 任务名称:{}, 任务时间:{}, 任务类:{}, 任务参数:{}", jobName, taskName, runTime, jobClassName, taskParam);
            before(context);
            run(context);
        } catch (Exception e) {
            log.error("定时任务异常, 任务ID:{}, 任务名称:{}, 任务时间:{}, 任务类:{}, 异常信息:{}", jobName, taskName, runTime, jobClassName, e.getMessage(), e);
        } finally {
            long end = SystemClock.now();
            long consume = end - start;
            complete(context);
            log.info("定时任务结束, 任务ID:{}, 任务名称:{}, 耗时{}ms, 任务时间:{}, 任务类:{}", jobName, taskName, consume, runTime, jobClassName);
            traceHandle(jobDataMap, start, end, consume);
        }
        // @formatter:on
    }

    /**
     * 链路处理
     */
    private void traceHandle(JobDataMap mergedJobDataMap, long start, long end, long consume) {
        if (!mergedJobDataMap.containsKey(TaskTemplate.TASK_MDC_INFO)) {
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
            MDC.setContextMap(mdcMap);
            return;
        }
        String appName = environment.getProperty("spring.application.name");
        String ip = IpUtil.localIp();
        mdcMap.put(LogConst.APP_NAME_KEY, appName);
        mdcMap.put(LogConst.IP_KEY, ip);
        TraceUtil.prepare(mdcMap);
    }



}
