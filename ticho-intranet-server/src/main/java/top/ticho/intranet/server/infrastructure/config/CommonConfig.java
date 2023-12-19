package top.ticho.intranet.server.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ticho.intranet.core.prop.ServerProperty;

/**
 * 通用配置
 *
 * @date 2023-12-17 08:30
 */
@Configuration
public class CommonConfig {

    @Bean
    @ConfigurationProperties(prefix = "ticho.intranet.server")
    public ServerProperty serverProperty() {
        return new ServerProperty();
    }

}
