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
import top.ticho.rainbow.interfaces.dto.command.MenuModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.MenuSaveCommand;
import top.ticho.rainbow.interfaces.dto.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.response.MenuDTO;
import top.ticho.rainbow.interfaces.dto.response.RouteDTO;
import top.ticho.rainbow.application.service.MenuService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.starter.view.core.TiResult;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 菜单信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("menu")
public class MenuController {
    private final MenuService menuService;

    /**
     * 保存菜单
     */
    @ApiLog("保存菜单")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody MenuSaveCommand menuSaveCommand) {
        menuService.save(menuSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除菜单
     */
    @ApiLog("删除菜单")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        menuService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改菜单
     */
    @ApiLog("修改菜单")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody MenuModifyCommand menuModifyCommand) {
        menuService.modify(menuModifyCommand);
        return TiResult.ok();
    }

    /**
     * 启用菜单
     */
    @ApiLog("启用菜单")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "菜单信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        menuService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用菜单
     */
    @ApiLog("禁用菜单")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "菜单信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        menuService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 查询所有菜单
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_ALL + "')")
    @GetMapping("all")
    public TiResult<List<MenuDTO>> all() {
        return TiResult.ok(menuService.list());
    }

    /**
     * 查询菜单路由(登录人)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_ROUTE + "')")
    @GetMapping("route")
    public TiResult<List<RouteDTO>> route() {
        return TiResult.ok(menuService.route());
    }

    /**
     * 查询权限编码
     *
     * @param roleCodes 角色代码
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_MENU_PERMS + "')")
    @GetMapping("perms")
    public TiResult<List<String>> getPerms(String roleCodes) {
        return TiResult.ok(menuService.getPerms(roleCodes));
    }

}
