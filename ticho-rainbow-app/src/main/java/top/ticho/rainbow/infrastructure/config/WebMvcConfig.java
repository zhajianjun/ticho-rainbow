package top.ticho.rainbow.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.ticho.intranet.common.prop.IntranetServerProperty;
import top.ticho.intranet.server.core.IntranetServerBuilder;
import top.ticho.intranet.server.core.IntranetServerHandler;
import top.ticho.intranet.server.filter.DefaultIntranetApplicationListenFilter;
import top.ticho.rainbow.infrastructure.common.component.DefaultTraceInterceptor;
import top.ticho.rainbow.infrastructure.common.prop.FileProperty;
import top.ticho.tool.core.TiStrUtil;

/**
 * 通用配置
 *
 * @date 2023-12-17 08:30
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileProperty fileProperty;
    private final DefaultTraceInterceptor tiTraceInterceptor;

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
        registry.addResourceHandler(mvcResourcePath).addResourceLocations(TiStrUtil.format("file:{}", publicAbsolutePath));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tiTraceInterceptor).order(tiTraceInterceptor.getOrder());
    }

    /**
     * 分页拦截器
     */
    @Bean
    public InnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor(DbType.MYSQL);
    }

    /**
     * 乐观锁拦截器
     */
    @Bean
    public InnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    @Bean
    @ConfigurationProperties(prefix = "ticho.intranet.server")
    public IntranetServerProperty intranetServerProperty() {
        return new IntranetServerProperty();
    }

    @Bean
    public IntranetServerHandler intranetServerHandler(IntranetServerProperty intranetServerProperty) {
        return IntranetServerBuilder.init(intranetServerProperty, new DefaultIntranetApplicationListenFilter());
    }

}
