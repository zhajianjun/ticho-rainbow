package top.ticho.rainbow.infrastructure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ticho.boot.security.handle.jwt.JwtSigner;
import top.ticho.boot.security.prop.BaseOauthProperty;
import top.ticho.rainbow.domain.handle.CacheHandle;
import top.ticho.rainbow.infrastructure.core.component.PermRedisCacheEvent;

/**
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Configuration
public class SecurityConfig {

    @Bean
    @ConditionalOnBean(BaseOauthProperty.class)
    public JwtSigner jwtSigner() {
        return new JwtSigner("intranet");
    }

    @Bean
    public CacheHandle permHandle() {
        return new CacheHandle();
    }

    @Bean
    public PermRedisCacheEvent permRedisCacheEvent(CacheHandle cacheHandle) {
        return new PermRedisCacheEvent(cacheHandle);
    }

}
