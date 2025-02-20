package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.starter.view.core.TiResult;

import javax.annotation.Resource;

/**
 * 工具
 *
 * @author zhajianjun
 * @date 2024-02-06 11:20
 */
@RestController
@RequestMapping("tool")
@Api(tags = "工具")
@ApiSort(110)
public class ToolController {

    @Resource
    private StringEncryptor stringEncryptor;

    @PreAuthorize("@perm.hasPerms('system:tool:encrypt')")
    @ApiOperation(value = "加密")
    @ApiOperationSupport(order = 10)
    @ApiImplicitParam(value = "加密信息", name = "message", required = true)
    @GetMapping("encrypt")
    public TiResult<String> encrypt(@RequestParam("message") String message) {
        return TiResult.ok(stringEncryptor.encrypt(message));
    }

    @PreAuthorize("@perm.hasPerms('system:tool:decrypt')")
    @ApiOperation(value = "解密")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "解密信息", name = "message", required = true)
    @GetMapping("decrypt")
    public TiResult<String> decrypt(@RequestParam("message") String message) {
        return TiResult.ok(stringEncryptor.decrypt(message));
    }

}
