package top.ticho.rainbow.domain.handle;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.rainbow.infrastructure.core.component.SseTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.util.CommonUtil;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.util.TiAssert;

import javax.annotation.Resource;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Sse处理
 *
 * @author zhajianjun
 * @date 2024-05-23 11:10
 */
@Component
public class SseHandle {
    public final String split = "-";

    @Resource
    private SseTemplate sseTemplate;

    @Resource
    private TiCacheTemplate tiCacheTemplate;

    public String getSign() {
        String id = StrUtil.format("{}{}{}", UserUtil.getCurrentUsername(), split, CommonUtil.fastShortUUID());
        tiCacheTemplate.put(CacheConst.SSE, id, System.currentTimeMillis());
        return id;
    }

    public SseEmitter connect(String id) {
        TiAssert.isTrue(tiCacheTemplate.contain(CacheConst.SSE, id), "权限不足");
        tiCacheTemplate.evict(CacheConst.SSE, id);
        return sseTemplate.connect(id);
    }

    public <T> void sendMessage(String id, SseMessage<T> message) {
        sseTemplate.sendMessage(id, message);
    }

    public <T> void sendMessage(String id, SseEvent sseEvent, T data) {
        sseTemplate.sendMessage(id, SseMessage.of(sseEvent, data));
    }

    public <T> void sendMessageForAll(SseMessage<T> message) {
        sseTemplate.sendMessageForAll(message);
    }

    public <T> void sendMessageForAll(SseEvent sseEvent, T data) {
        sseTemplate.sendMessageForAll(SseMessage.of(sseEvent, data));
    }

    public void sendMessageByUsername(String username, String message) {
        Predicate<String> idCheck = item -> item.startsWith(username + split);
        Function<String, String> messageHandle = item -> message;
        sseTemplate.sendMessageCondition(idCheck, messageHandle);
    }

    public void heatbeat() {
        sseTemplate.sendMessageForAll(SseMessage.of(SseEvent.HEATBEAT, null));
    }

    public void close(String id) {
        sseTemplate.close(id);
    }

}
