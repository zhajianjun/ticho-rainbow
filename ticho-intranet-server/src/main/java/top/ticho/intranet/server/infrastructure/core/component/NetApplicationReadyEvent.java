package top.ticho.intranet.server.infrastructure.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 网络应用程序
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Component
@Slf4j
public class NetApplicationReadyEvent implements ApplicationListener<ApplicationReadyEvent> {

    @Async
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
    }

}
