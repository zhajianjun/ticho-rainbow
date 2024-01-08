package top.ticho.intranet.server.infrastructure.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import top.ticho.boot.web.event.BaseApplicationReadyEvent;
import top.ticho.intranet.server.domain.handle.CacheHandle;

/**
 * 程序启动成功把当前应用的权限标识缓存到redis中
 *
 * @see ApplicationReadyEvent 应用程序已准备好执行的事件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
public class PermRedisCacheEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final CacheHandle cacheHandle;

    public PermRedisCacheEvent(CacheHandle cacheHandle) {
        this.cacheHandle = cacheHandle;
    }

    /**
     * 默认事件
     *
     * @param event ApplicationReadyEvent
     */
    @Override
    @Async
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        cacheHandle.pushCurrentAppPerms();
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Environment env = applicationContext.getEnvironment();
        String property = env.getProperty(BaseApplicationReadyEvent.SPRING_APPLICATION_NAME_KEY, "application");
        log.info("{} perms is cached", property);
    }
}
