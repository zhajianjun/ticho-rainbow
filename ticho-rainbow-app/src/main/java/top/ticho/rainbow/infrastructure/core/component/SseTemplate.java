package top.ticho.rainbow.infrastructure.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.boot.view.core.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhajianjun
 * @date 2024-05-21 16:06
 */
@Slf4j
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

    public SseEmitter createConnect(String id) {
        // 设置过期时间0-不过期,默认值位30秒
        SseEmitter sseEmitter = new SseEmitterUTF8(0L);
        // 长链接完成后回调接口(即关闭连接时调用)
        sseEmitter.onCompletion(() -> {
            log.info("sse[{}] connect completed", id);
            sseEmitterMap.remove(id);
        });
        // 连接超时回调
        sseEmitter.onTimeout(() -> {
            log.info("sse[{}] connect timeout", id);
            sseEmitterMap.remove(id);
        });
        // 推送消息异常时，回调方法
        sseEmitter.onError(throwable -> log.info("sse[{}] connect error: {}", id, throwable.getMessage(), throwable));
        // 存入容器中
        sseEmitterMap.put(id, sseEmitter);
        log.info("sse[{}-{}] is created", id, sseEmitter.hashCode());
        return sseEmitter;
    }

    /**
     * 根据id发送消息给某一客户端
     */
    public void sendMessageToOneClient(String id, String msg) {
        sendMsgToClientByid(id, msg, sseEmitterMap.get(id));
    }

    /**
     * 发送消息给所有客户端
     */
    public void sendMessageToAllClient(String msg) {
        List<Result<?>> list = new ArrayList<>();
        Result<?> responseResult = Result.ok(msg);
        list.add(responseResult);
        for (Map.Entry<String, SseEmitter> entry : sseEmitterMap.entrySet()) {
            sendMsgToClientByid(entry.getKey(), list, entry.getValue());
        }
    }

    /**
     * 关闭连接
     */
    public void closeConnect(String id) {
        SseEmitter sseEmitter = sseEmitterMap.get(id);
        if (sseEmitter != null) {
            sseEmitter.complete();
            sseEmitterMap.remove(id);
            log.info("sse[{}] connect close", id);
        }
    }


    /**
     * 根据客户端id 发送给某一客户端
     */
    private void sendMsgToClientByid(String id, Object data, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            return;
        }
        try {
            sseEmitter.send(data);
        } catch (IOException e) {
            log.error("sse[{}-{}] send msg error: {}", id, sseEmitter.hashCode(), e.getMessage(), e);
        }
    }

}
