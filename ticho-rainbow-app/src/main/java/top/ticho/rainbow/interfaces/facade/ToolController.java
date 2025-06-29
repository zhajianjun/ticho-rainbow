package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.starter.view.core.TiResult;

import jakarta.validation.constraints.NotBlank;

/**
 * 工具
 *
 * @author zhajianjun
 * @date 2024-02-06 11:20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("tool")
public class ToolController {
    private final StringEncryptor stringEncryptor;

    /**
     * 加密
     *
     * @param message 加密信息
     */
    @ApiLog("加密")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TOOL_ENCRYPT + "')")
    @PostMapping("encrypt")
    public TiResult<String> encrypt(@NotBlank(message = "参数不能为空") String message) {
        return TiResult.ok(stringEncryptor.encrypt(message));
    }

    /**
     * 解密
     *
     * @param message 解密信息
     */
    @ApiLog("解密")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TOOL_DECRYPT + "')")
    @PostMapping("decrypt")
    public TiResult<String> decrypt(@NotBlank(message = "参数不能为空") String message) {
        return TiResult.ok(stringEncryptor.decrypt(message));
    }

}
