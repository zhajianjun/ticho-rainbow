package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.interfaces.dto.command.RoleModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.RoleSaveCommand;
import top.ticho.rainbow.interfaces.dto.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.query.RoleDtlQuery;
import top.ticho.rainbow.interfaces.dto.query.RoleQuery;
import top.ticho.rainbow.interfaces.dto.response.RoleDTO;
import top.ticho.rainbow.interfaces.dto.response.RoleMenuDTO;
import top.ticho.rainbow.application.service.RoleService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.security.annotation.IgnoreType;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

/**
 * 角色信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("role")
public class RoleController {
    private final RoleService roleService;

    /**
     * 保存角色
     */
    @ApiLog("保存角色")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody RoleSaveCommand roleSaveCommand) {
        roleService.save(roleSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除角色
     */
    @ApiLog("删除角色")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        roleService.remove(command);
        roleService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改角色
     */
    @ApiLog("修改角色")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody RoleModifyCommand roleModifyCommand) {
        roleService.modify(roleModifyCommand);
        return TiResult.ok();
    }

    /**
     * 启用角色
     */
    @ApiLog("启用角色")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "角色信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        roleService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用角色
     */
    @ApiLog("禁用角色")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "角色信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        roleService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 查询角色(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<RoleDTO>> page(@Validated RoleQuery query) {
        return TiResult.ok(roleService.page(query));
    }


    /**
     * 查询所有角色
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_ALL + "')")
    @GetMapping("all")
    public TiResult<List<RoleDTO>> all() {
        return TiResult.ok(roleService.all());
    }

    /**
     * 查询角色菜单
     */
    @IgnoreJwtCheck(IgnoreType.INNER)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_LIST_ROLE_MENU + "')")
    @GetMapping("menu/list")
    public TiResult<RoleMenuDTO> listRoleMenu(RoleDtlQuery roleDtlQuery) {
        return TiResult.ok(roleService.listRoleMenu(roleDtlQuery));
    }

    /**
     * 导出角色
     */
    @ApiLog("导出角色信息")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_ROLE_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated RoleQuery query) throws IOException {
        roleService.exportExcel(query);
    }

}
