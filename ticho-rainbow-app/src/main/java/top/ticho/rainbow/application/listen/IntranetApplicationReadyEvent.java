package top.ticho.rainbow.application.listen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.executor.IntranetExecutor;

/**
 * 网络应用程序
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class IntranetApplicationReadyEvent {

    private final IntranetExecutor intranetExecutor;

    @Async("asyncTaskExecutor")
    @EventListener(value = ApplicationReadyEvent.class)
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        intranetExecutor.init();
        intranetExecutor.enable();
        log.info("内网应用程序启动成功");
    }

}
