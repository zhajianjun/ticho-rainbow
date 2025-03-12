package top.ticho.rainbow.domain.handle;

import lombok.Data;

/**
 * sse消息
 *
 * @author zhajianjun
 * @date 2024-05-23 10:58
 */
@Data
public class SseMessage<T> {

    /** 事件类型 */
    private SseEvent event;    /** 数据 */
    private T data;
    public static <T> SseMessage<T> of(SseEvent sseEvent, T data) {
        SseMessage<T> message = new SseMessage<>();
        message.setData(data);
        message.setEvent(sseEvent);
        return message;
    }

}
