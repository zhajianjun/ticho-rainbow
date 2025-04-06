package top.ticho.rainbow.application.listen;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.service.PermissionQueryService;
import top.ticho.starter.web.event.TiApplicationReadyEvent;

/**
 * 权限编码缓存实践
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 * @see ApplicationReadyEvent 应用程序已准备好执行的事件
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PermListener {
    private final PermissionQueryService permissionQueryService;

    /**
     * 默认事件
     */
    @Async("asyncTaskExecutor")
    @EventListener(value = ApplicationReadyEvent.class)
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        permissionQueryService.cache();
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Environment env = applicationContext.getEnvironment();
        String property = env.getProperty(TiApplicationReadyEvent.SPRING_APPLICATION_NAME_KEY, "application");
        log.info("{} perms is cached", property);
    }

}
