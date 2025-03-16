package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.MenuDTO;
import top.ticho.rainbow.application.dto.RouteDTO;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;
import top.ticho.rainbow.application.service.MenuService;
import top.ticho.starter.view.core.TiResult;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("menu")
public class MenuController {
    private final MenuService menuService;

    /**
     * 保存菜单
     */
    @PreAuthorize("@perm.hasPerms('system:menu:save')")
    @PostMapping
    public TiResult<Void> save(@RequestBody MenuDTO menuDTO) {
        menuService.save(menuDTO);
        return TiResult.ok();
    }

    /**
     * 删除菜单
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:menu:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        menuService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@perm.hasPerms('system:menu:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody MenuDTO menuDTO) {
        menuService.modify(menuDTO);
        return TiResult.ok();
    }

    /**
     * 查询菜单
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:menu:getById')")
    @GetMapping
    public TiResult<MenuDTO> getById(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(menuService.getById(id));
    }

    /**
     * 查询所有菜单
     */
    @PreAuthorize("@perm.hasPerms('system:menu:all')")
    @GetMapping("all")
    public TiResult<List<MenuDtlDTO>> list() {
        return TiResult.ok(menuService.list());
    }

    /**
     * 查询菜单路由(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:menu:route')")
    @GetMapping("route")
    public TiResult<List<RouteDTO>> route() {
        return TiResult.ok(menuService.route());
    }

    /**
     * 查询权限编码
     *
     * @param roleCodes 角色代码
     */
    @PreAuthorize("@perm.hasPerms('system:menu:getPerms')")
    @GetMapping("getPerms")
    public TiResult<List<String>> getPerms(List<String> roleCodes) {
        return TiResult.ok(menuService.getPerms(roleCodes));
    }

}
