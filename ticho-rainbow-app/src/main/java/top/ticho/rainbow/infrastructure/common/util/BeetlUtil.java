package top.ticho.rainbow.infrastructure.common.util;

import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import top.ticho.starter.view.exception.TiBizException;

import java.io.IOException;

/**
 * Beetl模板引擎工具
 *
 * @author zhajianjun
 * @date 2024-02-06 14:26:32
 */
@Slf4j
public class BeetlUtil {
    private BeetlUtil() {
    }

    /**
     * 获取模板组
     *
     * @param isTemplate 是否从模板文件读取模板信息，默认是true-是 false-否
     */
    public static GroupTemplate getGroupTemplate(boolean isTemplate) {
        return new GroupTemplate(getResourceLoader(isTemplate), getConfiguration());
    }

    /**
     * 获取 资源加载器
     * 负责根据GroupTemplate提供的Key,来获取Resource，这些Resource可以是文件
     *
     * @param isTemplate 是否从模板文件读取模板信息，默认是true-是 false-否
     */
    public static ResourceLoader<String> getResourceLoader(boolean isTemplate) {
        ResourceLoader<String> resourceLoader;
        if (isTemplate) {
            resourceLoader = getClassPathResource();
        } else {
            resourceLoader = getStringTemplateResource();
        }
        return resourceLoader;
    }

    /**
     * 获取beetl默认配置对象
     */
    public static Configuration getConfiguration() {
        try {
            return Configuration.defaultConfiguration();
        } catch (IOException e) {
            String message = "获取beetl默认配置对象失败！";
            log.error(message, e);
            throw new TiBizException(message);
        }
    }


    public static ResourceLoader<String> getClassPathResource() {
        return SingletonClassInstance.CLASSPATH_INSTANCE;
    }

    public static ResourceLoader<String> getStringTemplateResource() {
        return SingletonClassInstance.STRING_TEMPLATE_INSTANCE;
    }

    private static class SingletonClassInstance {
        public static final ResourceLoader<String> CLASSPATH_INSTANCE = new ClasspathResourceLoader();
        public static final ResourceLoader<String> STRING_TEMPLATE_INSTANCE = new StringTemplateResourceLoader();
    }
}
