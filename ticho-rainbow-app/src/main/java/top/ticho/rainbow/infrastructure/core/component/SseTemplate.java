package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.boot.view.exception.BizException;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Sse模板工具
 *
 * @author zhajianjun
 * @date 2024-05-21 16:06
 */
@Slf4j
@Component
public class SseTemplate {
    /** sseEmitterMap */
    private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static class SseEmitterUTF8 extends SseEmitter {
        public SseEmitterUTF8(Long timeout) {
            super(timeout);
        }

        @Override
        protected void extendResponse(@NonNull ServerHttpResponse outputMessage) {
            super.extendResponse(outputMessage);
            HttpHeaders headers = outputMessage.getHeaders();
            headers.setContentType(new MediaType(MediaType.TEXT_EVENT_STREAM, StandardCharsets.UTF_8));
        }
    }

    /**
     * 连接
     */
    public SseEmitter connect(String id) {
        SseEmitter oldEmitter = sseEmitterMap.get(id);
        if (oldEmitter != null) {
            oldEmitter.completeWithError(new BizException(StrUtil.format("sse[{}] repeat connect", id)));
        }
        // 设置过期时间0-不过期,默认值位30秒
        SseEmitter sseEmitter = new SseEmitterUTF8(0L);
        // 连接断开
        sseEmitter.onCompletion(() -> {
            log.info("sse[{}] connect completed", id);
            sseEmitterMap.remove(id);
        });
        // 连接超时
        sseEmitter.onTimeout(() -> {
            log.info("sse[{}] connect timeout", id);
            sseEmitterMap.remove(id);
        });
        // 连接报错
        sseEmitter.onError(throwable -> {
            log.info("sse[{}] connect error: {}", id, throwable.getMessage());
            sseEmitterMap.remove(id);
        });
        // 存入容器中
        sseEmitterMap.put(id, sseEmitter);
        log.info("sse[{}-{}] is created", id, sseEmitter.hashCode());
        return sseEmitter;
    }

    /**
     * 根据id发送消息给某一客户端
     */
    public void sendMessage(String id, Object message) {
        sendMessage(id, message, sseEmitterMap.get(id));
    }

    /**
     * 灵活发送消息给指定某些客户端
     */
    public void sendMessageCondition(Predicate<String> idCheck, Function<String, String> messageHandle) {
        for (Map.Entry<String, SseEmitter> entry : sseEmitterMap.entrySet()) {
            if (!idCheck.test(entry.getKey())) {
                continue;
            }
            String message = messageHandle.apply(entry.getKey());
            sendMessage(entry.getKey(), message, entry.getValue());
        }
    }

    /**
     * 发送消息给所有客户端
     */
    public void sendMessageForAll(Object message) {
        for (Map.Entry<String, SseEmitter> entry : sseEmitterMap.entrySet()) {
            sendMessage(entry.getKey(), message, entry.getValue());
        }
    }

    /**
     * 根据客户端id 发送给某一客户端
     */
    private void sendMessage(String id, Object message, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            return;
        }
        try {
            sseEmitter.send(message);
        } catch (Exception e) {
            log.error("sse[{}-{}] send msg error: {}", id, sseEmitter.hashCode(), e.getMessage());
        }
    }

    /**
     * 关闭连接
     */
    public void close(String id) {
        SseEmitter sseEmitter = sseEmitterMap.get(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
            sseEmitterMap.remove(id);
            log.info("sse[{}] connect close", id);
        }
    }

}
