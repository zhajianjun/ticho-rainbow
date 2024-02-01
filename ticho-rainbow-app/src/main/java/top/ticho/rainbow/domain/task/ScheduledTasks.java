package top.ticho.rainbow.domain.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.function.Consumer;

/**
 * 定時任务
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
// @Component
@Slf4j
public class ScheduledTasks {

    /**
     * 清除过期的channel
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearExpiredDataTask() {
        taskLog((x) -> clearExpiredData(), "清除过期的客户端通道");
    }

    /**
     * 关闭未启动的客户端通道，并解绑端口
     */
    @Scheduled(initialDelay = 3000L, fixedRate = 120000L)
    public void closeUnEnabledClientsTask() {
        taskLog((x) -> closeUnEnabledClients(), "关闭未启动的客户端通道，并解绑端口");
    }

    /**
     * 关闭未启动的客户端通道，并解绑端口
     */
    private void closeUnEnabledClients() {
    }

    /**
     * 清除过期的客户端和端口数据
     */
    public void clearExpiredData() {
    }

    public void taskLog(Consumer<?> supplier, String message) {
        log.info("定时任务开启：{}", message);
        try {
            supplier.accept(null);
        } catch (Exception e) {
            log.error("定时任务异常：{}", message, e);
        } finally {
            log.info("定时任务结束：{}", message);
        }
    }


}
