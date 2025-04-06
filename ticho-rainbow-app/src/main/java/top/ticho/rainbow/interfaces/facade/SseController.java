package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.rainbow.application.event.SseEvent;
import top.ticho.rainbow.application.service.SseHandle;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.validation.constraints.NotBlank;

/**
 * sse
 *
 * @author zhajianjun
 * @date 2024-05-21 15:10
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("sse")
public class SseController {

    private final SseHandle sseHandle;

    /**
     * 获取sse签名
     */
    @GetMapping("sign")
    public TiResult<String> sign() {
        return TiResult.ok(sseHandle.getSign());
    }

    /**
     * 连接sse
     *
     * @param id 编号
     */
    @IgnoreJwtCheck
    @TiView(ignore = true)
    @GetMapping("connect")
    public SseEmitter connect(String id) {
        return sseHandle.connect(id);
    }

    /**
     * 发送sse消息
     *
     * @param id      编号
     * @param message 消息
     * @return {@link TiResult }<{@link Void }>
     */
    @GetMapping("send")
    public TiResult<Void> send(@NotBlank(message = "编号不能为空") String id, String message) {
        sseHandle.sendMessage(id, SseEvent.HEATBEAT, message);
        return TiResult.ok();
    }

    /**
     * 关闭sse
     *
     * @param id 编号
     */
    @GetMapping("close")
    public TiResult<Void> close(String id) {
        sseHandle.close(id);
        return TiResult.ok();
    }

}
