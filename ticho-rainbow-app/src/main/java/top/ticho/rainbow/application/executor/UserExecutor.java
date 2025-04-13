package top.ticho.rainbow.application.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.util.TiAssert;

/**
 * @author zhajianjun
 * @date 2025-04-06 19:37
 */
@Component
@RequiredArgsConstructor
public class UserExecutor {
    private final TiCacheTemplate tiCacheTemplate;

    public void imgCodeValid(String key, String imgCode) {
        String cacheImgCode = tiCacheTemplate.get(CacheConst.VERIFY_CODE, key, String.class);
        TiAssert.isNotBlank(cacheImgCode, "验证码已过期");
        tiCacheTemplate.evict(CacheConst.VERIFY_CODE, key);
        TiAssert.isTrue(imgCode.equalsIgnoreCase(cacheImgCode), "验证码不正确");
    }

}
