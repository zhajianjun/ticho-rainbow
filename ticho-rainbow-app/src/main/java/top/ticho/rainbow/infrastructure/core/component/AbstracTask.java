package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import top.ticho.tool.trace.common.bean.TraceInfo;
import top.ticho.tool.trace.common.constant.LogConst;
import top.ticho.tool.trace.common.prop.TraceProperty;
import top.ticho.tool.trace.core.handle.TracePushContext;
import top.ticho.tool.trace.core.util.TraceUtil;
import top.ticho.tool.trace.spring.event.TraceEvent;
import top.ticho.tool.trace.spring.util.IpUtil;

import javax.annotation.Resource;

/**
 * @author zhajianjun
 * @date 2024-03-23 22:04
 */
@Slf4j
public abstract class AbstracTask extends QuartzJobBean {

    @Resource
    private Environment environment;

    @Resource
    private TraceProperty traceProperty;

    public void executeInternal(JobExecutionContext context) {
        // @formatter:off
        long start = SystemClock.now();
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        String taskName = mergedJobDataMap.getString(SchedulerTemplate.TASK_NAME);
        String runTime = DateUtil.format(context.getScheduledFireTime(), DatePattern.NORM_DATETIME_FORMAT);
        JobDetail jobDetail = context.getJobDetail();
        String jobName = jobDetail.getKey().getName();
        try {
            String appName = environment.getProperty("spring.application.name");
            String ip = IpUtil.localIp();
            TraceUtil.prepare(null, null, appName, ip, "定时任务", ip, null);
            log.info("定时任务开始，任务: {}-{}, 执行时间：{}", jobName, taskName, runTime);
            run(context);
        } catch (Exception e) {
            log.info("定时任务异常，任务: {}-{}, 执行时间: {}, 任务: {}", jobName, taskName, runTime, e.getMessage(), e);
        } finally {
            log.info("定时任务结束，任务: {}-{}, 执行时间：{}", jobName, taskName, runTime);
            String env = environment.getProperty("spring.profiles.active");
            long end = SystemClock.now();
            Long consume = end - start;
            TraceInfo traceInfo = TraceInfo.builder()
                .traceId(MDC.get(LogConst.TRACE_ID_KEY))
                .spanId(MDC.get(LogConst.SPAN_ID_KEY))
                .appName(MDC.get(LogConst.APP_NAME_KEY))
                .env(env)
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
        // @formatter:on
    }

    public abstract void run(JobExecutionContext context);

}
