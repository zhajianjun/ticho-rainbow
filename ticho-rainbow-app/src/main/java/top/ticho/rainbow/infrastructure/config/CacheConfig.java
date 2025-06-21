package top.ticho.rainbow.infrastructure.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.dto.FileCacheDTO;
import top.ticho.starter.cache.config.TiCache;
import top.ticho.starter.cache.config.TiCacheRegistry;

import java.time.Duration;
import java.util.Arrays;

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
        UPLOAD_CHUNK(CacheConst.UPLOAD_CHUNK, 180, 10000),
        SSE(CacheConst.SSE, 180, 10000),
        ;

        CacheEnum(String key, int ttl, int maxSize) {
            this.key = key;
            this.maxSize = maxSize;
            this.ttl = ttl;
        }

        /** key */
        private final String key;
        /** 过期时间（秒） */
        private final int ttl;
        /** 最大數量 */
        private final int maxSize;
    }

    @Bean
    public TiCacheRegistry tiCacheRegistry() {
        return (tiCaches) -> {
            CacheEnum[] values = CacheEnum.values();
            Arrays.stream(values)
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
                }).forEach(tiCaches::add);
            tiCaches.add(fileInfoCache());
        };
    }

    public TiCache<String, FileCacheDTO> fileInfoCache() {
        return new TiCache<>() {
            @Override
            public String getName() {
                return CacheConst.FILE_URL_CACHE;
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
            public long expireAfterCreate(String key, FileCacheDTO value, long currentTime) {
                return Duration.ofMillis(value.getExpire()).toNanos();
            }

            @Override
            public long expireAfterUpdate(String key, FileCacheDTO value, long currentTime, long currentDuration) {
                return Duration.ofMillis(value.getExpire()).toNanos();
            }

            @Override
            public long expireAfterRead(String key, FileCacheDTO value, long currentTime, long currentDuration) {
                return Duration.ofMillis(value.getExpire()).toNanos();
            }

        };
    }

}
