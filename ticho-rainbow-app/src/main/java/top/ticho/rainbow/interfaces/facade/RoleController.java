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
import top.ticho.rainbow.application.dto.command.RoleModifyCommand;
import top.ticho.rainbow.application.dto.command.RoleSaveCommand;
import top.ticho.rainbow.application.dto.command.RoleStatusModifyCommand;
import top.ticho.rainbow.application.dto.query.RoleDtlQuery;
import top.ticho.rainbow.application.dto.query.RoleQuery;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.application.dto.response.RoleMenuDtlDTO;
import top.ticho.rainbow.application.service.RoleService;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.security.annotation.IgnoreType;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 角色信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("role")
public class RoleController {
    private final RoleService roleService;

    /**
     * 保存角色
     */
    @PreAuthorize("@perm.hasPerms('system:role:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody RoleSaveCommand roleSaveCommand) {
        roleService.save(roleSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除角色
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:role:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        roleService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改角色
     */
    @PreAuthorize("@perm.hasPerms('system:role:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody RoleModifyCommand roleModifyCommand) {
        roleService.modify(roleModifyCommand);
        return TiResult.ok();
    }

    /**
     * 修改角色状态
     */
    @PreAuthorize("@perm.hasPerms('system:role:modify-status')")
    @PatchMapping("status")
    public TiResult<Void> modifyStatus(@RequestBody RoleStatusModifyCommand statusModifyCommand) {
        roleService.modifyStatus(statusModifyCommand);
        return TiResult.ok();
    }

    /**
     * 查询角色
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:role:find')")
    @GetMapping
    public TiResult<RoleDTO> find(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(roleService.find(id));
    }

    /**
     * 查询角色(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:role:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<RoleDTO>> page(@Validated RoleQuery query) {
        return TiResult.ok(roleService.page(query));
    }


    /**
     * 查询所有角色
     */
    @PreAuthorize("@perm.hasPerms('system:role:all')")
    @GetMapping("all")
    public TiResult<List<RoleDTO>> all() {
        return TiResult.ok(roleService.all());
    }

    /**
     * 查询角色菜单
     */
    @IgnoreJwtCheck(IgnoreType.INNER)
    @PreAuthorize("@perm.hasPerms('system:role:list-role-menu')")
    @GetMapping("menu/list")
    public TiResult<RoleMenuDtlDTO> listRoleMenu(RoleDtlQuery roleDtlQuery) {
        return TiResult.ok(roleService.listRoleMenu(roleDtlQuery));
    }

    /**
     * 导出角色
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:role:export')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated RoleQuery query) throws IOException {
        roleService.exportExcel(query);
    }

}
