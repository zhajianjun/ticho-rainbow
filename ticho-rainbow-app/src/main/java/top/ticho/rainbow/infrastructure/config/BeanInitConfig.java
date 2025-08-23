package top.ticho.rainbow.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.common.component.DefaultTraceInterceptor;
import top.ticho.trace.common.TiConsoleTraceReporter;
import top.ticho.trace.common.TiTraceProperty;
import top.ticho.trace.common.TiTraceReporter;

/**
 * @author zhajianjun
 * @date 2025-07-20 16:07
 */
@Component
public class BeanInitConfig {

    @Bean
    public TiTraceReporter tiTraceReporter() {
        return new TiConsoleTraceReporter();
    }


    @Bean
    public DefaultTraceInterceptor defaultTraceInterceptor(TiTraceProperty tiTraceProperty) {
        return new DefaultTraceInterceptor(tiTraceProperty);
    }

}
