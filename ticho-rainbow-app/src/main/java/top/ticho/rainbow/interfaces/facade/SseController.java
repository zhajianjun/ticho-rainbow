package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.infrastructure.core.component.SseTemplate;

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

    private final SseTemplate sseTemplate = new SseTemplate();

    @View(ignore = true)
    @ApiOperation(value = "connect")
    @ApiOperationSupport(order = 10)
    @GetMapping("connect")
    public SseEmitter connect(String id) {
        return sseTemplate.createConnect(id);
    }

    @ApiOperation(value = "send")
    @ApiOperationSupport(order = 20)
    @GetMapping("send")
    public Result<Void> send(String id, String message) {
        sseTemplate.sendMessageToOneClient(id, message);
        return Result.ok();
    }

    @ApiOperation(value = "close")
    @ApiOperationSupport(order = 30)
    @GetMapping("close")
    public Result<Void> close(String id) {
        sseTemplate.closeConnect(id);
        return Result.ok();
    }

}
