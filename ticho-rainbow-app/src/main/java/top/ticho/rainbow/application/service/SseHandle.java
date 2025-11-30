package top.ticho.rainbow.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.rainbow.application.dto.SseMessage;
import top.ticho.rainbow.application.event.SseEvent;
import top.ticho.rainbow.infrastructure.common.component.SseTemplate;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.util.UserUtil;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.tool.core.TiAssert;
import top.ticho.tool.core.TiIdUtil;
import top.ticho.tool.core.TiStrUtil;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Sse处理
 *
 * @author zhajianjun
 * @date 2024-05-23 11:10
 */
@RequiredArgsConstructor
@Component
public class SseHandle {
    public final String split = "-";

    private final SseTemplate sseTemplate;
    private final TiCacheTemplate tiCacheTemplate;

    public String getSign() {
        String id = TiStrUtil.format("{}{}{}", UserUtil.getCurrentUsername(), split, TiIdUtil.shortUuid());
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
