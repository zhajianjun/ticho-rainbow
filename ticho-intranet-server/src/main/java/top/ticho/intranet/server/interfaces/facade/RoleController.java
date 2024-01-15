package top.ticho.intranet.server.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.security.annotation.IgnoreJwtCheck;
import top.ticho.boot.security.annotation.IgnoreType;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.intranet.server.application.service.RoleService;
import top.ticho.intranet.server.interfaces.dto.RoleDTO;
import top.ticho.intranet.server.interfaces.dto.RoleMenuDTO;
import top.ticho.intranet.server.interfaces.dto.RoleMenuDtlDTO;
import top.ticho.intranet.server.interfaces.query.RoleDtlQuery;
import top.ticho.intranet.server.interfaces.query.RoleQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色信息")
@ApiSort(40)
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("@perm.hasPerms('system:role:save')")
    @ApiOperation(value = "保存角色信息")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody RoleDTO roleDTO) {
        roleService.save(roleDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:remove')")
    @ApiOperation(value = "删除角色信息")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> remove(@RequestParam("id") Long id) {
        roleService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:update')")
    @ApiOperation(value = "修改角色信息")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody RoleDTO roleDTO) {
        roleService.updateById(roleDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:updateStatus')")
    @ApiOperation(value = "修改角色状态")
    @ApiOperationSupport(order = 30)
    @PutMapping("updateStatus")
    public Result<Void> updateStatus(@RequestBody RoleDTO roleDTO) {
        roleService.updateStatus(roleDTO.getId(), roleDTO.getStatus());
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:get')")
    @ApiOperation(value = "主键查询角色信息")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<RoleDTO> getById(@RequestParam("id") Serializable id) {
        return Result.ok(roleService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:role:page')")
    @ApiOperation(value = "分页查询角色信息")
    @ApiOperationSupport(order = 50)
    @GetMapping("page")
    public Result<PageResult<RoleDTO>> page(RoleQuery query) {
        return Result.ok(roleService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:role:list')")
    @ApiOperation(value = "查询角色信息")
    @ApiOperationSupport(order = 52)
    @GetMapping("list")
    public Result<List<RoleDTO>> list(RoleQuery query) {
        return Result.ok(roleService.list(query));
    }

    @PreAuthorize("@perm.hasPerms('system:role:bindMenu')")
    @ApiOperation(value = "角色绑定菜单信息")
    @ApiOperationSupport(order = 60)
    @PostMapping("bindMenu")
    public Result<Void> bindMenu(@RequestBody RoleMenuDTO roleMenuDTO) {
        roleService.bindMenu(roleMenuDTO);
        return Result.ok();
    }

    @IgnoreJwtCheck(IgnoreType.INNER)
    @ApiOperation(value = "根据角色code查询角色菜单信息")
    @ApiOperationSupport(order = 70)
    @PostMapping("listRoleMenuByCodes")
    public Result<RoleMenuDtlDTO> listByCodes(@RequestBody RoleDtlQuery roleDtlQuery) {
        return Result.ok(roleService.listByCodes(roleDtlQuery));
    }

    @IgnoreJwtCheck(IgnoreType.INNER)
    @ApiOperation(value = "根据角色id查询角色菜单信息")
    @ApiOperationSupport(order = 80)
    @PostMapping("listRoleMenuByIds")
    public Result<RoleMenuDtlDTO> listByIds(@RequestBody RoleDtlQuery roleDtlQuery) {
        return Result.ok(roleService.listByIds(roleDtlQuery));
    }

}
