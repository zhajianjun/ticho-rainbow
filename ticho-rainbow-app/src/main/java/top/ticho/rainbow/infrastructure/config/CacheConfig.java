package top.ticho.rainbow.infrastructure.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.po.FileCache;
import top.ticho.starter.cache.config.TiCache;
import top.ticho.starter.cache.config.TiCacheBatch;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 缓存配置
 *
 * @author zhajianjun
 * @date 2024-01-15 20:00
 */
@Configuration
public class CacheConfig {

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
        private final String key;        /** 过期策略;1-write,2-access */
        private final Integer expireStrategy;        /** 过期时间（秒） */
        private final int ttl;        /** 最大數量 */
        private final int maxSize;    }

    @Bean
    public TiCacheBatch tiCacheBatch() {
        return () -> {
            CacheEnum[] values = CacheEnum.values();
            return Arrays.stream(values)
                .map(cacheEnum -> new TiCache<String, Object>() {
                    @Override
                    public String getName() {
                        return cacheEnum.getKey();
                    }

                    @Override
                    public int getMaxSize() {
                        return cacheEnum.getMaxSize();
                    }

                    @Override
                    public int getTtl() {
                        return cacheEnum.getTtl();
                    }
                })
                .collect(Collectors.toList());
        };
    }

    @Bean
    public TiCache<String, FileCache> fileInfoCache() {
        return new TiCache<String, FileCache>() {
            @Override
            public String getName() {
                return "";
            }

            @Override
            public int getMaxSize() {
                return 1000;
            }

            @Override
            public int getTtl() {
                return 0;
            }

            @Override
            public long expireAfterCreate(String key, FileCache value, long currentTime) {
                return Duration.ofMillis(value.getExpire()).toNanos();
            }

            @Override
            public long expireAfterUpdate(String key, FileCache value, long currentTime, long currentDuration) {
                return Duration.ofMillis(value.getExpire()).toNanos();
            }

            @Override
            public long expireAfterRead(String key, FileCache value, long currentTime, long currentDuration) {
                return Duration.ofMillis(value.getExpire()).toNanos();
            }

        };
    }

}
