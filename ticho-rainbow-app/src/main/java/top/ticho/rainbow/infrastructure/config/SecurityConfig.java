package top.ticho.rainbow.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.ticho.rainbow.infrastructure.common.prop.SecurityProperty;
import top.ticho.starter.security.handle.jwt.JwtSigner;
import top.ticho.starter.web.util.TiIdUtil;

import java.util.Optional;

/**
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Configuration
public class SecurityConfig {

    @Bean
    @Primary
    public JwtSigner jwtSigner(SecurityProperty securityProperty) {
        String key = Optional.of(securityProperty.getKey()).orElseGet(TiIdUtil::getUuid);
        return new JwtSigner(key);
    }

}
