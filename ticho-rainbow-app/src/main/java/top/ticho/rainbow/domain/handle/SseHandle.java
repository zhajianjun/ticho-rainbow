package top.ticho.rainbow.domain.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.rainbow.infrastructure.core.component.SseTemplate;

/**
 * Sse处理
 *
 * @author zhajianjun
 * @date 2024-05-23 11:10
 */
@Component
public class SseHandle {

    @Autowired
    private SseTemplate sseTemplate;

    public SseEmitter connect(String id) {
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

    public void heatbeat() {
        sseTemplate.sendMessageForAll(SseMessage.of(SseEvent.HEATBEAT, null));
    }

    public void close(String id) {
        sseTemplate.close(id);
    }


}
