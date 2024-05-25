package top.ticho.rainbow.domain.handle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * sse消息
 *
 * @author zhajianjun
 * @date 2024-05-23 10:58
 */
@Data
@ApiModel(value = "sse消息")
public class SseMessage<T> {

    @ApiModelProperty(value = "类型", position = 10)
    private SseEvent sseEvent;

    @ApiModelProperty(value = "数据", position = 20)
    private T data;

    public static <T> SseMessage<T> of(SseEvent sseEvent, T data) {
        SseMessage<T> message = new SseMessage<>();
        message.setData(data);
        message.setSseEvent(sseEvent);
        return message;
    }

}
