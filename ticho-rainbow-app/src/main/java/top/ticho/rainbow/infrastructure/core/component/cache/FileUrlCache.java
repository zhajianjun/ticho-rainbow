package top.ticho.rainbow.infrastructure.core.component.cache;

import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.entity.FileCache;
import top.ticho.starter.cache.config.TiCache;

import java.time.Duration;

/**
 * @author zhajianjun
 * @date 2024-12-29 16:39
 */
@Component
public class FileUrlCache implements TiCache<String, FileCache> {

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
        return -1;
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

}
