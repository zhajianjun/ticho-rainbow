package top.ticho.intranet.server.infrastructure.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.ticho.tool.intranet.prop.ServerProperty;
import top.ticho.tool.intranet.server.handler.ServerHandler;

/**
 * 通用配置
 *
 * @date 2023-12-17 08:30
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 参数1请求名称、参数2视图名称
        registry.addViewController("/").setViewName("/health");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 1.创建MybatisPlusInterceptor拦截器对象
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        // 2.添加乐观锁拦截器
        mpInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mpInterceptor;
    }

    @Bean
    @ConfigurationProperties(prefix = "ticho.intranet.server")
    public ServerProperty serverProperty() {
        return new ServerProperty();
    }

    @Bean
    public ServerHandler serverHandler(ServerProperty serverProperty) {
        return new ServerHandler(serverProperty);
    }

}
