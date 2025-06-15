package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.MenuAssembler;
import top.ticho.rainbow.application.executor.AuthExecutor;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.domain.entity.vo.MenuModifyVO;
import top.ticho.rainbow.domain.repository.MenuRepository;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.common.dto.SecurityUser;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.infrastructure.common.enums.MenuType;
import top.ticho.rainbow.infrastructure.common.enums.YesOrNo;
import top.ticho.rainbow.infrastructure.common.util.UserUtil;
import top.ticho.rainbow.interfaces.command.MenuModifyCommand;
import top.ticho.rainbow.interfaces.command.MenuSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.MenuDTO;
import top.ticho.rainbow.interfaces.dto.RouteDTO;
import top.ticho.rainbow.interfaces.dto.RouteMetaDTO;
import top.ticho.starter.view.enums.TiBizErrorCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiTreeUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuAssembler menuAssembler;
    private final RoleMenuRepository roleMenuRepository;
    private final AuthExecutor authExecutor;


    public void save(MenuSaveCommand menuSaveCommand) {
        Menu menu = menuAssembler.toEntity(menuSaveCommand);
        checkData(menu);
        TiAssert.isTrue(menuRepository.save(menu), "保存失败");
    }

    public void modify(MenuModifyCommand menuModifyCommand) {
        Menu menu = menuRepository.find(menuModifyCommand.getId());
        TiAssert.isNotNull(menu, "菜单不存在");
        menu.checkVersion(menuModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        checkData(menu);
        MenuModifyVO menuModifyVO = menuAssembler.toModifyVO(menuModifyCommand);
        menu.modify(menuModifyVO);
        TiAssert.isTrue(menuRepository.modify(menu), "修改失败，请刷新后重试");
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(VersionModifyCommand command) {
        Menu menu = menuRepository.find(command.getId());
        TiAssert.isNotNull(menu, "删除失败，数据不存在");
        menu.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(!menu.isEnable(), "删除失败，请先禁用该菜单");
        List<Long> menuIds = Collections.singletonList(command.getId());
        boolean existsByMenuIds = roleMenuRepository.existsByMenuIds(menuIds);
        TiAssert.isTrue(!existsByMenuIds, "删除失败,请解绑所有的角色菜单");
        TiAssert.isTrue(menuRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, Menu::enable);
        TiAssert.isTrue(enable, "启用菜单失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, Menu::disable);
        TiAssert.isTrue(disable, "禁用菜单失败，请刷新后重试");
    }

    public List<MenuDTO> list() {
        List<Menu> menus = menuRepository.cacheList();
        List<MenuDTO> menuFuncDtls = authExecutor.toDTO(menus, null, null);
        Consumer<MenuDTO> consumer = (root) -> {
            List<MenuDTO> children = root.getChildren();
            children = CollUtil.isEmpty(children) ? null : children;
            root.setChildren(children);
        };
        MenuDTO root = new MenuDTO();
        root.setId(0L);
        TiTreeUtil.tree(menuFuncDtls, root, (x, y) -> true, (x, y) -> {
        }, consumer);
        return root.getChildren();
    }

    public List<RouteDTO> route() {
        SecurityUser currentUser = UserUtil.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return Collections.emptyList();
        }
        List<String> roleCodes = currentUser.getRoles();
        if (CollUtil.isEmpty(roleCodes)) {
            return Collections.emptyList();
        }
        List<Menu> menus;
        if (roleCodes.contains(SecurityConst.ADMIN)) {
            menus = menuRepository.cacheList();
        } else {
            menus = authExecutor.getMenusByRoleCodes(roleCodes, Function.identity()).collect(Collectors.toList());
        }
        List<RouteDTO> routes = menus
            .stream()
            .filter(x -> Objects.equals(CommonStatus.ENABLE.code(), x.getStatus()))
            .sorted(Comparator.comparing(Menu::getParentId).thenComparing(Comparator.nullsLast(Comparator.comparing(Menu::getSort))))
            .map(this::toRouteDTO)
            .collect(Collectors.toList());
        Consumer<RouteDTO> afterConsumer = (root) -> {
            if (!Objects.equals(root.getType(), MenuType.MENU.code())) {
                return;
            }
            List<RouteDTO> children = root.getChildren();
            List<String> buttons = children.stream().map(RouteDTO::getName).collect(Collectors.toList());
            root.setButtons(buttons);
            root.setChildren(Collections.emptyList());
        };
        RouteDTO root = new RouteDTO();
        root.setId(0L);
        TiTreeUtil.tree(routes, root, (x, y) -> true, (x, y) -> {
        }, afterConsumer);
        return root.getChildren();
    }

    private RouteDTO toRouteDTO(Menu menu) {
        boolean ignoreKeepAlive = !Objects.equals(menu.getKeepAlive(), 1);
        boolean hideMenu = !Objects.equals(menu.getInvisible(), 1);
        boolean extFlag = Objects.equals(menu.getExtFlag(), 1);
        RouteDTO routeDTO = menuAssembler.toRouteDTO(menu);
        RouteMetaDTO routeMetaDTO = menuAssembler.toRouteMetaDTO(menu);
        routeMetaDTO.setIgnoreKeepAlive(ignoreKeepAlive);
        routeMetaDTO.setHideMenu(hideMenu);
        if (extFlag) {
            routeMetaDTO.setFrameSrc(menu.getRedirect());
            routeDTO.setRedirect(null);
        }
        routeDTO.setMeta(routeMetaDTO);
        return routeDTO;
    }

    private void checkData(Menu menu) {
        Integer type = menu.getType();
        Long parentId = menu.getParentId();
        boolean isRoot = Objects.equals(CommConst.PARENT_ID, parentId);
        Menu parentMenu = null;
        Integer parentType = MenuType.DIR.code();
        String parentTypeName = MenuType.DIR.message();
        if (!isRoot) {
            parentMenu = menuRepository.find(parentId);
            TiAssert.isNotNull(parentMenu, TiBizErrorCode.PARAM_ERROR, "父节点不存在");
            parentType = parentMenu.getType();
            parentTypeName = MenuType.getByCode(parentMenu.getType());
        }
        // 1-目录，父亲一定是目录，目录和路由的路由地址不能重复
        if (Objects.equals(MenuType.DIR.code(), type)) {
            checkDirectory(menu);
            TiAssert.isTrue(Objects.equals(parentType, MenuType.DIR.code()), StrUtil.format("{}下不能新建目录", parentTypeName));
            Menu getByTypesAndPath = menuRepository.getByTypesAndPath(MenuType.dirOrMenus(), menu.getPath(), menu.getId());
            // 菜单或路由path不能重复
            TiAssert.isNull(getByTypesAndPath, "目录路由重复");
            if (!Objects.equals(menu.getExtFlag(), 1)) {
                TiAssert.isNotBlank(menu.getComponentName(), "组件名称不能为空");
                Menu repeatCompMenu = menuRepository.getByTypesAndComNameExcludeId(MenuType.dirOrMenus(), menu.getComponentName(), menu.getId());
                // 按钮名称不能重复
                TiAssert.isNull(repeatCompMenu, "组件名称重复");
            }
        }
        // 2-菜单，父亲一定是目录，目录和路由的路由地址不能重复
        else if (Objects.equals(MenuType.MENU.code(), type)) {
            checkMenu(menu);
            // 不是外部链接，则组件路径和组件名称不能为空
            if (!Objects.equals(YesOrNo.YES.code(), menu.getExtFlag())) {
                checkExt(menu);
            }
            if (Objects.equals(YesOrNo.YES.code(), menu.getInvisible())) {
                menu.modifyCurrentActiveMenu("");
            }
            TiAssert.isTrue(Objects.equals(parentType, MenuType.DIR.code()), StrUtil.format("{}下不能新建菜单", parentTypeName));
            Menu getByTypesAndPath = menuRepository.getByTypesAndPath(MenuType.dirOrMenus(), menu.getPath(), menu.getId());
            // 菜单或路由path不能重复
            TiAssert.isNull(getByTypesAndPath, "菜单路由重复");
            Menu repeatCompMenu = menuRepository.getByTypesAndComNameExcludeId(MenuType.dirOrMenus(), menu.getComponentName(), menu.getId());
            // 按钮名称不能重复
            TiAssert.isNull(repeatCompMenu, "组件名称重复");
        }
        // 3-按钮，父亲一定是菜单, 组件名称不能重复
        else if (Objects.equals(MenuType.BUTTON.code(), type)) {
            checkButton(menu);
            TiAssert.isTrue(Objects.equals(parentType, MenuType.MENU.code()), StrUtil.format("{}下不能新建按钮", parentTypeName));
            Menu repeatCompMenu = menuRepository.getByTypesAndComNameExcludeId(Collections.singletonList(MenuType.BUTTON.code()), menu.getComponentName(), menu.getId());
            // 按钮名称不能重复
            TiAssert.isNull(repeatCompMenu, "按钮名称重复");
        } else {
            TiAssert.cast(TiBizErrorCode.PARAM_ERROR, "未知菜单类型");
        }
        fillMenu(menu, parentMenu);
    }

    public void checkDirectory(Menu menu) {
        TiAssert.isNotBlank(menu.getPath(), "路由地址不能为空");
        TiAssert.isNotBlank(menu.getIcon(), "图标不能为空");
    }

    public void checkMenu(Menu menu) {
        TiAssert.isNotBlank(menu.getPath(), "路由地址不能为空");
    }

    public void checkExt(Menu menu) {
        TiAssert.isNotBlank(menu.getComponent(), "组件路径不能为空");
        TiAssert.isNotBlank(menu.getComponentName(), "组件名称不能为空");
    }

    public void checkButton(Menu menu) {
        TiAssert.isNotBlank(menu.getComponentName(), "组件名称不能为空");
    }


    private void fillMenu(Menu menu, Menu parent) {
        // 父节点不存在则该节点为父节点
        String structure;
        long parentId;
        if (Objects.isNull(parent)) {
            structure = String.valueOf(menu.getId());
            parentId = CommConst.PARENT_ID;
        } else {
            structure = parent.getStructure() + "-" + menu.getId();
            parentId = parent.getId();
        }
        menu.modify(parentId, structure);
    }

    public List<String> getPerms(String roleCodes) {
        return authExecutor.getPerms(Arrays.stream(roleCodes.split(",")).collect(Collectors.toList()));
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<Menu> modifyHandle) {
        List<Long> ids = CollStreamUtil.toList(modifys, VersionModifyCommand::getId);
        List<Menu> menus = menuRepository.list(ids);
        Map<Long, Menu> menuMap = CollStreamUtil.toIdentityMap(menus, Menu::getId);
        for (VersionModifyCommand modify : modifys) {
            Menu menu = menuMap.get(modify.getId());
            TiAssert.isNotNull(menu, StrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            menu.checkVersion(modify.getVersion(), StrUtil.format("数据已被修改，请刷新后重试, 菜单: {}", menu.getName()));
            // 修改逻辑
            modifyHandle.accept(menu);
        }
        return menuRepository.modifyBatch(menus);
    }

}
