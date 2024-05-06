package top.ticho.rainbow.infrastructure.core.component.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 公共缓存模板工具
 *
 * @author zhajianjun
 * @date 2024-04-28 08:43
 */
@Getter
@Slf4j
public class CommonCacheTemplate<T> {

    private final Cache<String, T> cache;

    public CommonCacheTemplate(Function<T, Long> expireFunction) {
        this(null, expireFunction, null, null);
    }

    /**
     * @param cacheLoadFunction   缓存加载逻辑
     * @param cacheExpireFunction 缓存过期逻辑
     * @param cacheRemoveConsumer 缓存移除处理
     * @param executor            线程池
     */
    public CommonCacheTemplate(
        Function<String, T> cacheLoadFunction,
        Function<T, Long> cacheExpireFunction,
        BiConsumer<String, T> cacheRemoveConsumer,
        Executor executor
    ) {
        // @formatter:off
        Caffeine<String, T>  stringTCaffeine = Caffeine.newBuilder()
            //初始容量
            .initialCapacity(16)
            // 最大长度
            .maximumSize(50)
            // 打开统计功能
            .recordStats()
            // 设置自定义过期
            .expireAfter(new DefaultExpiry<>(cacheExpireFunction))
            // 设置固定过期 失效后同步加载缓存，阻塞机制获取缓存
            //.expireAfterWrite(Duration.ofMinutes(1))
            // 设置固定过期 失效后异步加载，其它线程任然获取旧值
            //.refreshAfterWrite(Duration.ofMinutes(1))
            // 缓存写入删除回调 同步
            //.writer(new DefaultCacheWriter())
            // 缓存移除监听 异步
            .removalListener(new DefaultRemovalListener<>(cacheRemoveConsumer));
        if (Objects.nonNull(executor)) {
            // 异步线程池
            stringTCaffeine.executor(executor);
        }
        if (Objects.nonNull(cacheLoadFunction)) {
            cache = stringTCaffeine.build(new DefaultCacheLoader<>(cacheLoadFunction));
        } else {
            cache = stringTCaffeine.build();
        }
    }

    public T get(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, T value) {
        cache.put(key, value);
    }

    public void putAll(Map<String, T> cacheDataMap) {
        cache.putAll(cacheDataMap);
    }

    public void clear() {
        cache.cleanUp();
    }

    /*  以下是Caffeine缓存需要的配置类  */

    static class DefaultExpiry<T> implements Expiry<String, T> {
        /** 过期时间转换逻辑 **/
        private final Function<T, Long> cacheExpireFunction;

        public DefaultExpiry(Function<T, Long> cacheExpireFunction) {
            this.cacheExpireFunction = cacheExpireFunction;
        }

        /**
         * 创建后过期时间
         *
         * @param key         key
         * @param value       value
         * @param currentTime 当前时间 （单位:ns纳秒）
         * @return 过期剩余时间（单位:ns纳秒），以纳秒为单位
         */
        @Override
        public long expireAfterCreate(@NonNull String key, @NonNull T value, long currentTime) {
            Long expire = cacheExpireFunction.apply(value);
            return Duration.ofMillis(expire).toNanos();
        }

        /**
         * 更新后过期时间
         *
         * @param key             key
         * @param value           value
         * @param currentTime     当前时间 （单位:ns纳秒）
         * @param currentDuration 当前持续时间
         * @return 过期剩余时间（单位:ns纳秒），以纳秒为单位
         */
        @Override
        public long expireAfterUpdate(@NonNull String key, @NonNull T value, long currentTime, @NonNegative long currentDuration) {
            Long expire = cacheExpireFunction.apply(value);
            return Duration.ofMillis(expire).toNanos();
        }

        /**
         * 读取后过期时间
         *
         * @param key             key
         * @param value           value
         * @param currentTime     当前时间 （单位:ns纳秒）
         * @param currentDuration 当前持续时间
         * @return 过期剩余时间（单位:ns纳秒），以纳秒为单位
         */
        @Override
        public long expireAfterRead(@NonNull String key, @NonNull T value, long currentTime, @NonNegative long currentDuration) {
            Long expire = cacheExpireFunction.apply(value);
            return Duration.ofMillis(expire).toNanos();
        }
    }

    /**
     * 缓存写入操作
     */
    static class DefaultCacheLoader<T> implements CacheLoader<String, T> {
        /** 过期时间转换逻辑 **/
        private final Function<String, T> function;

        public DefaultCacheLoader(Function<String, T> function) {
            this.function = function;
        }

        @Override
        public T load(@NonNull String key) {
            return function.apply(key);
        }
    }

    /**
     * 缓存移除监听
     * <p>
     * RemovalListener的方法是异步执行的
     * </p>
     */
    static class DefaultRemovalListener<T> implements RemovalListener<String, T> {
        /** 缓存移除处理 */
        private final BiConsumer<String, T> consumer;

        public DefaultRemovalListener(BiConsumer<String, T> consumer) {
            if (Objects.isNull(consumer)) {
                consumer = (x, y) -> {};
            }
            this.consumer = consumer;
        }

        @Override
        public void onRemoval(String key, T value, @NonNull RemovalCause cause) {
            log.info("缓存移除监听, 移除的key = {}, value = {}, cause = {}", key, value, cause);
            consumer.accept(key, value);
        }
    }

}
