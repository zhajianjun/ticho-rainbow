package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 定时任务模板工具
 *
 * @author zhajianjun
 * @date 2024-03-23 15:58
 */
@AllArgsConstructor
@Slf4j
@Component
public class SchedulerTemplate {
    public static final String SCHEDULER_PARAM = "SCHEDULER_PARAM";
    public static final String TASK_NAME = "TASK_NAME";
    public static final String MDC_INFO = "MDC_INFO";

    private Scheduler scheduler;

    // @formatter:off

    /**
     * 新增定时任务
     *
     * @param jobName        任务名称
     * @param jobGroup       任务组
     * @param jobClass       任务类
     * @param cronExpression 定时表达式
     * @param param          任务参数
     * @return boolean
     */
    public boolean addJob(String jobName, String jobGroup, String jobClass, String cronExpression, String taskName, String param) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(TASK_NAME, taskName);
        paramMap.put(SCHEDULER_PARAM, param);
        return addJob(jobName, jobGroup, jobName, jobGroup, jobClass, cronExpression, paramMap);
    }

    /**
     * 新增定时任务
     *
     * @param jobName        任务名称
     * @param jobGroup       任务组
     * @param triggerName    触发器名称
     * @param triggerGroup   触发器组
     * @param jobClass       任务类
     * @param cronExpression 定时表达式
     * @param paramMap       任务参数MAP
     * @return boolean
     */
    public boolean addJob(
        String jobName,
        String jobGroup,
        String triggerName,
        String triggerGroup,
        String jobClass,
        String cronExpression,
        Map<String, Object> paramMap
    ) {
        Class<Job> jobClazz = ClassUtil.loadClass(jobClass);
        JobDetail jobDetail = JobBuilder.newJob(jobClazz)
            .withIdentity(jobName, jobGroup)
            .build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.putAll(paramMap);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerName, triggerGroup)
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build();
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            return true;
        } catch (SchedulerException e) {
            log.error("新增定时任务失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查任务是否存在
     *
     * @param jobName  任务名称
     * @param jobGroup 任务组
     * @return boolean
     */
    public boolean checkExists(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            log.error("检查任务是否存在失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 执行一次任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务组
     * @return boolean
     */
    public boolean runOnce(String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        try {
            Map<String, String> mdcMap = MDC.getCopyOfContextMap();
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(MDC_INFO, mdcMap);
            scheduler.triggerJob(jobKey, jobDataMap);
            return true;
        } catch (SchedulerException e) {
            log.error("运行一次定时任务失败", e);
            return false;
        }
    }

    /**
     * 重启定时任务
     *
     * @param triggerName    触发器名称
     * @param triggerGroup   触发器组
     * @param cronExpression 定时表达式
     */
    public boolean rescheduleJob(String triggerName, String triggerGroup, String cronExpression) {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build();
        try {
            scheduler.rescheduleJob(triggerKey, cronTrigger);
            return true;
        } catch (SchedulerException e) {
            log.error("重启定时任务失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 暂停定时任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务组
     */
    public boolean pauseJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
            return true;
        } catch (SchedulerException e) {
            log.error("重启定时任务失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 恢复定时任务
     *
     * @param jobName  任务名称
     * @param jobGroup 任务组
     */
    public boolean resumeJob(String jobName, String jobGroup) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
            return true;
        } catch (SchedulerException e) {
            log.error("恢复定时任务失败: {}", e.getMessage(), e);
            return false;
        }
    }


    /**
     * 删除定时任务
     *
     * @param jobName      任务名称
     * @param jobGroup     任务组
     */
    public boolean deleteJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            return true;
        } catch (SchedulerException e) {
            log.error("删除定时任务失败: {}", e.getMessage(), e);
            return false;
        }
    }


    /**
     * 删除定时任务
     *
     * @param jobName      任务名称
     * @param jobGroup     任务组
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组
     */
    public boolean deleteJob(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroup));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            return true;
        } catch (SchedulerException e) {
            log.error("删除定时任务失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 查询所有定时任务
     *
     * @return 所有任务
     */
    public List<String> listJobs() {
        Set<JobKey> jobKeys;
        try {
            jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        } catch (SchedulerException e) {
            log.error("获取定时任务列表失败: ", e);
            return Collections.emptyList();
        }
        return jobKeys.stream().map(JobKey::getName).collect(Collectors.toList());
    }

    /**
     * 表达式是否有效
     *
     * @param cronExpression cron表达式
     */
    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * 根据cron表达式获取近n次的执行时间
     *
     * @param cronExpression cron表达式
     * @param numTimes       次数
     * @return {@link List}<{@link String}> 最近次数的执行时间 列表
     */
    public static List<String> getRecentCronTime(String cronExpression, int numTimes) {
        try {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(cronExpression);
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, numTimes);
            return dates
                .stream()
                .map(date-> DateUtil.format(date, DatePattern.NORM_DATETIME_FORMAT))
                .collect(Collectors.toList());
        } catch (ParseException e) {
            log.error("获取近{}次的执行时间失败: {}", numTimes, e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
