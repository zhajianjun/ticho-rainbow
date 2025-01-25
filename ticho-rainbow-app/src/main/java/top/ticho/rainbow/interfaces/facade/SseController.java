package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.rainbow.domain.handle.SseEvent;
import top.ticho.rainbow.domain.handle.SseHandle;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.annotation.Resource;

/**
 * sse
 *
 * @author zhajianjun
 * @date 2024-05-21 15:10
 */
@RestController
@RequestMapping("sse")
@Api(tags = "sse")
@ApiSort(170)
public class SseController {

    @Resource
    private SseHandle sseHandle;

    @ApiOperation(value = "sign")
    @ApiOperationSupport(order = 10)
    @GetMapping("sign")
    public TiResult<String> sign() {
        return TiResult.ok(sseHandle.getSign());
    }

    @IgnoreJwtCheck
    @TiView(ignore = true)
    @ApiOperation(value = "connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperationSupport(order = 20)
    @GetMapping("connect")
    public SseEmitter connect(String id) {
        return sseHandle.connect(id);
    }

    @ApiOperation(value = "send")
    @ApiOperationSupport(order = 30)
    @GetMapping("send/{id}")
    public TiResult<Void> send(@PathVariable("id") String id, String message) {
        sseHandle.sendMessage(id, SseEvent.HEATBEAT, message);
        return TiResult.ok();
    }

    @ApiOperation(value = "close")
    @ApiOperationSupport(order = 40)
    @GetMapping("close")
    public TiResult<Void> close(String id) {
        sseHandle.close(id);
        return TiResult.ok();
    }

}
