package top.ticho.rainbow.infrastructure.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.ticho.starter.web.event.TiApplicationReadyEvent;

import javax.annotation.Resource;

/**
 * 权限编码缓存实践
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 * @see ApplicationReadyEvent 应用程序已准备好执行的事件
 */
@Slf4j
@Component
public class PermCacheEvent implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private PermCacheHandle permCacheHandle;

    /**
     * 默认事件
     */
    @Override
    @Async
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        permCacheHandle.pushCurrentAppPerms();
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Environment env = applicationContext.getEnvironment();
        String property = env.getProperty(TiApplicationReadyEvent.SPRING_APPLICATION_NAME_KEY, "application");
        log.info("{} perms is cached", property);
    }

}
