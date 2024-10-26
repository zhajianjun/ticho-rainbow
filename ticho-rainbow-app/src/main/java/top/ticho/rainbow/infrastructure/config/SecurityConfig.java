package top.ticho.rainbow.infrastructure.config;

import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.ticho.boot.security.handle.jwt.JwtSigner;

/**
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Configuration
public class SecurityConfig {

    @Bean
    @Primary
    public JwtSigner jwtSigner() {
        return new JwtSigner(IdUtil.fastSimpleUUID());
    }

}
