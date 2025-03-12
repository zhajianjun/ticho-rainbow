package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.RoleDTO;
import top.ticho.rainbow.application.dto.RoleMenuDTO;
import top.ticho.rainbow.application.dto.RoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.command.RoleModifyCommand;
import top.ticho.rainbow.application.dto.command.RoleStatusModifyCommand;
import top.ticho.rainbow.application.dto.query.RoleDtlQuery;
import top.ticho.rainbow.application.dto.query.RoleQuery;
import top.ticho.rainbow.application.service.RoleService;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.security.annotation.IgnoreType;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

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
     * 查询全部角色(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:role:page')")
    @GetMapping
    public TiResult<TiPageResult<RoleDTO>> page(@RequestBody RoleQuery query) {
        return TiResult.ok(roleService.page(query));
    }

    /**
     * 保存角色
     */
    @PreAuthorize("@perm.hasPerms('system:role:save')")
    @PostMapping
    public TiResult<Void> save(@RequestBody RoleDTO roleDTO) {
        roleService.save(roleDTO);
        return TiResult.ok();
    }

    /**
     * 删除角色
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:role:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@RequestParam("id") Long id) {
        roleService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改角色
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:role:modify')")
    @PutMapping("{id}")
    public TiResult<Void> modify(@PathVariable("id") Long id, @RequestBody RoleModifyCommand roleModifyCommand) {
        roleService.modify(id, roleModifyCommand);
        return TiResult.ok();
    }

    /**
     * 修改角色状态
     */
    @PreAuthorize("@perm.hasPerms('system:role:updateStatus')")
    @PatchMapping("status/{id}")
    public TiResult<Void> updateStatus(@PathVariable("id") Long id, @RequestBody RoleStatusModifyCommand statusModifyCommand) {
        roleService.modifyStatus(id, statusModifyCommand);
        return TiResult.ok();
    }

    /**
     * 查询角色
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:role:getById')")
    @GetMapping("{id}")
    public TiResult<RoleDTO> getById(@PathVariable("id") Long id) {
        return TiResult.ok(roleService.getById(id));
    }


    /**
     * 查询全部角色
     */
    @PreAuthorize("@perm.hasPerms('system:role:all')")
    @PostMapping("all")
    public TiResult<List<RoleDTO>> list(@RequestBody RoleQuery query) {
        return TiResult.ok(roleService.list(query));
    }

    /**
     * 绑定角色菜单
     */
    @PreAuthorize("@perm.hasPerms('system:role:bindMenu')")
    @PostMapping("bindMenu")
    public TiResult<Void> bindMenu(@RequestBody RoleMenuDTO roleMenuDTO) {
        roleService.bindMenu(roleMenuDTO);
        return TiResult.ok();
    }

    /**
     * 查询角色菜单
     */
    @IgnoreJwtCheck(IgnoreType.INNER)
    @PreAuthorize("@perm.hasPerms('system:role:listRoleMenu')")
    @PostMapping("listRoleMenu")
    public TiResult<RoleMenuDtlDTO> listRoleMenu(@RequestBody RoleDtlQuery roleDtlQuery) {
        return TiResult.ok(roleService.listRoleMenu(roleDtlQuery));
    }

    /**
     * 导出角色
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:role:expExcel')")
    @PostMapping("expExcel")
    public void expExcel(@RequestBody RoleQuery query) throws IOException {
        roleService.expExcel(query);
    }

}
