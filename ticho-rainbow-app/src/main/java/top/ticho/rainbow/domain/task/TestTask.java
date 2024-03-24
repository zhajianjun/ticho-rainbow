package top.ticho.rainbow.domain.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.rainbow.infrastructure.component.AbstracTask;

/**
 * 测试定时任务
 *
 * @author zhajianjun
 * @date 2024-03-23 23:22
 */
@Component
@Slf4j
public class TestTask extends AbstracTask {

    @Override
    public void run(JobExecutionContext context) {
        log.info("TestTask is running..., {}", JsonUtil.toJsonString(context.getMergedJobDataMap()));
    }

}
