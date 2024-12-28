package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import top.ticho.boot.view.core.TiPageResult;
import top.ticho.boot.view.core.TiResult;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.application.system.service.RoleService;
import top.ticho.rainbow.interfaces.dto.RoleDTO;
import top.ticho.rainbow.interfaces.dto.RoleMenuDTO;
import top.ticho.rainbow.interfaces.dto.RoleMenuDtlDTO;
import top.ticho.rainbow.interfaces.query.RoleDtlQuery;
import top.ticho.rainbow.interfaces.query.RoleQuery;

import java.io.IOException;
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
    @ApiOperation(value = "保存角色")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody RoleDTO roleDTO) {
        roleService.save(roleDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:remove')")
    @ApiOperation(value = "删除角色")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> remove(@RequestParam("id") Long id) {
        roleService.removeById(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:update')")
    @ApiOperation(value = "修改角色")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody RoleDTO roleDTO) {
        roleService.updateById(roleDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:updateStatus')")
    @ApiOperation(value = "修改角色状态")
    @ApiOperationSupport(order = 40)
    @PutMapping("updateStatus")
    public TiResult<Void> updateStatus(@RequestBody RoleDTO roleDTO) {
        roleService.updateStatus(roleDTO.getId(), roleDTO.getStatus());
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:role:getById')")
    @ApiOperation(value = "查询角色")
    @ApiOperationSupport(order = 50)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<RoleDTO> getById(@RequestParam("id") Serializable id) {
        return TiResult.ok(roleService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:role:page')")
    @ApiOperation(value = "查询全部角色(分页)")
    @ApiOperationSupport(order = 60)
    @PostMapping("page")
    public TiResult<TiPageResult<RoleDTO>> page(@RequestBody RoleQuery query) {
        return TiResult.ok(roleService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:role:list')")
    @ApiOperation(value = "查询全部角色")
    @ApiOperationSupport(order = 70)
    @PostMapping("list")
    public TiResult<List<RoleDTO>> list(@RequestBody RoleQuery query) {
        return TiResult.ok(roleService.list(query));
    }

    @PreAuthorize("@perm.hasPerms('system:role:bindMenu')")
    @ApiOperation(value = "绑定角色菜单")
    @ApiOperationSupport(order = 80)
    @PostMapping("bindMenu")
    public TiResult<Void> bindMenu(@RequestBody RoleMenuDTO roleMenuDTO) {
        roleService.bindMenu(roleMenuDTO);
        return TiResult.ok();
    }

    @IgnoreJwtCheck(IgnoreType.INNER)
    @PreAuthorize("@perm.hasPerms('system:role:listRoleMenu')")
    @ApiOperation(value = "查询角色菜单")
    @ApiOperationSupport(order = 90)
    @PostMapping("listRoleMenu")
    public TiResult<RoleMenuDtlDTO> listRoleMenu(@RequestBody RoleDtlQuery roleDtlQuery) {
        return TiResult.ok(roleService.listRoleMenu(roleDtlQuery));
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:role:expExcel')")
    @ApiOperation(value = "导出角色", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 100)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody RoleQuery query) throws IOException {
        roleService.expExcel(query);
    }

}
