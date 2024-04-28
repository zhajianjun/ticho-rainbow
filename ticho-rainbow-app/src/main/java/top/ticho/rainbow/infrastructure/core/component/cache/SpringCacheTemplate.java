package top.ticho.rainbow.infrastructure.core.component.cache;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 缓存工具
 *
 * @author zhajianjun
 * @date 2024-02-04 11:36
 */
@Component
public class SpringCacheTemplate {

    @Autowired
    private CacheManager cacheManager;

    public Optional<Cache> getOptCache(String name) {
        if (StrUtil.isBlank(name)) {
            return Optional.empty();
        }
        return Optional.ofNullable(cacheManager.getCache(name));
    }

    public boolean contain(String name, Object key) {
        return getOptCache(name).map(x -> x.get(key)).map(Cache.ValueWrapper::get).isPresent();
    }

    public <T> T get(String name, Object key, Class<T> type) {
        return getOptCache(name).map(x -> x.get(key, type)).orElse(null);
    }

    public <T> T get(String name, Object key, Callable<T> valueLoader) {
        return getOptCache(name).map(x -> x.get(key, valueLoader)).orElse(null);
    }

    public void put(String name, Object key, Object value) {
        getOptCache(name).ifPresent(x -> x.put(key, value));
    }

    public void evict(String name, Object key) {
        getOptCache(name).ifPresent(x -> x.evict(key));
    }

    public void clear(String name) {
        getOptCache(name).ifPresent(Cache::clear);
    }

    public long size(String name) {
        return getOptCache(name)
            .map(x-> (CaffeineCache) x)
            .map(CaffeineCache::getNativeCache)
            .map(com.github.benmanes.caffeine.cache.Cache::estimatedSize)
            .orElse(0L);
    }

}
