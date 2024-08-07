package top.ticho.rainbow.infrastructure.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 *
 * @author zhajianjun
 * @date 2024-01-15 20:00
 */
@Configuration
@EnableCaching
public class CacheConfig {
    private final SimpleCacheManager cacheManager = new SimpleCacheManager();

    // 定义cache名称、超时时长（秒）、最大容量
    @Getter
    public enum CacheEnum {
        COMMON(CacheConst.COMMON, 1800, 10000),
        VERIFY_CODE(CacheConst.VERIFY_CODE, 300, 10000),
        SIGN_UP_CODE(CacheConst.SIGN_UP_CODE, 300, 10000),
        RESET_PASSWORD_CODE(CacheConst.RESET_PASSWORD_CODE, 300, 10000),
        USER_INFO(CacheConst.USER_INFO, 1800, 10000),
        USER_ROLE_INFO(CacheConst.USER_ROLE_INFO, 1800, 10000),
        ROLE_MENU_INFO(CacheConst.ROLE_MENU_INFO, 1800, 10000),
        UPLOAD_CHUNK(CacheConst.UPLOAD_CHUNK, 2, 180, 10000),
        SSE(CacheConst.SSE, 180, 10000),
        ;

        CacheEnum(String key, int ttl, int maxSize) {
            this.key = key;
            this.expireStrategy = 1;
            this.maxSize = maxSize;
            this.ttl = ttl;
        }

        CacheEnum(String key, int expireStrategy, int ttl, int maxSize) {
            this.key = key;
            this.expireStrategy = expireStrategy;
            this.maxSize = maxSize;
            this.ttl = ttl;
        }

        /** key */
        private final String key;
        /** 过期策略;1-write,2-access */
        private final Integer expireStrategy;
        /** 过期时间（秒） */
        private final int ttl;
        /** 最大數量 */
        private final int maxSize;
    }

    // 创建基于Caffeine的Cache Manager
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        // @formatter:off
        List<CaffeineCache> caches = new ArrayList<>();
        for(CacheEnum c : CacheEnum.values()){
            Caffeine<Object,Object> builder = Caffeine.newBuilder()
                .recordStats()
                .maximumSize(c.getMaxSize())
                .removalListener(new DefaultRemovalListener(c.getKey()));
            if (c.getExpireStrategy() == 2) {
                builder.expireAfterAccess(c.getTtl(), TimeUnit.SECONDS);
            } else {
                builder.expireAfterWrite(c.getTtl(), TimeUnit.SECONDS);
            }
            Cache<Object, Object> build = builder.build();
            CaffeineCache caffeineCache = new CaffeineCache(c.getKey(), build);
            caches.add(caffeineCache);
        }
        cacheManager.setCaches(caches);
        return cacheManager;
        // @formatter:on
    }

    @Bean
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Slf4j
    @AllArgsConstructor
    static class DefaultRemovalListener implements RemovalListener<Object, Object> {

        private final String name;

        @Override
        public void onRemoval(Object key, Object value, @NonNull RemovalCause cause) {
            log.info("缓存{}移除监听, 移除的key = {}, cause = {}", name, key, cause);
        }
    }

}
