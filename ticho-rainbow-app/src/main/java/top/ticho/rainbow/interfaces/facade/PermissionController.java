package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.PermissionQueryService;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.interfaces.dto.response.PermissionDTO;
import top.ticho.starter.view.core.TiResult;

import java.util.List;

/**
 * 权限标识信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("permission")
public class PermissionController {
    private final PermissionQueryService permissionQueryService;

    /**
     * 查询所有权限标识
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_PERMISSION_TREE + "')")
    @GetMapping("tree")
    public TiResult<List<PermissionDTO>> tree() {
        return TiResult.ok(permissionQueryService.tree());
    }

}
