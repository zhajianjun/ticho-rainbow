package top.ticho.rainbow.infrastructure.common.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-04-06 19:24
 */
@Data
@ConfigurationProperties(prefix = "ticho.rainbow.op-log")
@Component
public class OpLogProperty {

    private List<String> ignorePatterns;

}
