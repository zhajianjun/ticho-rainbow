package top.ticho.rainbow.infrastructure.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.ticho.rainbow.domain.service.IntranetAppListenFilter;
import top.ticho.rainbow.infrastructure.core.interceptor.CustomTraceInterceptor;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;
import top.ticho.tool.intranet.prop.ServerProperty;
import top.ticho.tool.intranet.server.handler.ServerHandler;

/**
 * 通用配置
 *
 * @date 2023-12-17 08:30
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CustomTraceInterceptor customTraceInterceptor;    private final FileProperty fileProperty;
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 参数1请求名称、参数2视图名称
        registry.addViewController("/").setViewName("/health");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String mvcResourcePath = fileProperty.getMvcResourcePath();
        String publicAbsolutePath = fileProperty.getPublicPath();
        log.info("静态文件路径: {}", publicAbsolutePath);
        registry.addResourceHandler(mvcResourcePath).addResourceLocations(StrUtil.format("file:{}", publicAbsolutePath));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customTraceInterceptor).order(customTraceInterceptor.getOrder());
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
    public ServerHandler serverHandler(ServerProperty serverProperty, IntranetAppListenFilter appListenFilter) {
        return new ServerHandler(serverProperty, appListenFilter);
    }

}
