package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.system.service.MenuService;
import top.ticho.rainbow.interfaces.dto.MenuDTO;
import top.ticho.rainbow.interfaces.dto.MenuDtlDTO;
import top.ticho.rainbow.interfaces.dto.RouteDTO;
import top.ticho.starter.view.core.TiResult;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单信息 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("menu")
@Api(tags = "菜单信息")
@ApiSort(50)
public class MenuController {

    @Resource
    private MenuService menuService;

    @PreAuthorize("@perm.hasPerms('system:menu:save')")
    @ApiOperation(value = "保存菜单")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody MenuDTO menuDTO) {
        menuService.save(menuDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:menu:remove')")
    @ApiOperation(value = "删除菜单")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> remove(@RequestParam("id") Long id) {
        menuService.removeById(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:menu:update')")
    @ApiOperation(value = "修改菜单")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody MenuDTO menuDTO) {
        menuService.updateById(menuDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:menu:getById')")
    @ApiOperation(value = "查询菜单")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<MenuDTO> getById(@RequestParam("id") Long id) {
        return TiResult.ok(menuService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:menu:list')")
    @ApiOperation(value = "查询所有菜单")
    @ApiOperationSupport(order = 50)
    @GetMapping("list")
    public TiResult<List<MenuDtlDTO>> list() {
        return TiResult.ok(menuService.list());
    }

    @PreAuthorize("@perm.hasPerms('system:menu:route')")
    @ApiOperation(value = "查询菜单路由(登录人)")
    @ApiOperationSupport(order = 60)
    @GetMapping("route")
    public TiResult<List<RouteDTO>> route() {
        return TiResult.ok(menuService.route());
    }

    @PreAuthorize("@perm.hasPerms('system:menu:getPerms')")
    @ApiOperation(value = "查询权限编码")
    @ApiOperationSupport(order = 70)
    @GetMapping("getPerms")
    public TiResult<List<String>> getPerms(List<String> roleCodes) {
        return TiResult.ok(menuService.getPerms(roleCodes));
    }

}
