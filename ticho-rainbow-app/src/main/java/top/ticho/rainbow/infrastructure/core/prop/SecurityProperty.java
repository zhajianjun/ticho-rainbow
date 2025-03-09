package top.ticho.rainbow.infrastructure.core.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhajianjun
 * @date 2025-03-02 12:38
 */
@Data
@ConfigurationProperties(prefix = "ticho.rainbow.security")
@Component
public class SecurityProperty {

    private String key;

}
