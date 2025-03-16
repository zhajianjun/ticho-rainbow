package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.MenuAssembler;
import top.ticho.rainbow.application.dto.MenuDTO;
import top.ticho.rainbow.application.dto.RouteDTO;
import top.ticho.rainbow.application.dto.RouteMetaDTO;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.domain.repository.MenuRepository;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.infrastructure.core.constant.CommConst;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.core.enums.MenuType;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiIdUtil;
import top.ticho.starter.web.util.TiTreeUtil;
import top.ticho.starter.web.util.valid.TiValidGroup;
import top.ticho.starter.web.util.valid.TiValidUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public class MenuService extends AbstractAuthServiceImpl {
    private final MenuRepository menuRepository;
    private final MenuAssembler menuAssembler;
    private final RoleMenuRepository roleMenuRepository;

    public void save(MenuDTO menuDTO) {
        TiValidUtil.valid(menuDTO, TiValidGroup.Add.class);
        menuDTO.setId(TiIdUtil.getId());
        Menu menu = checkAndGetMenu(menuDTO);
        TiAssert.isTrue(menuRepository.save(menu), TiBizErrCode.FAIL, "保存失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        // 子节点为空才能删除
        List<Long> menuIds = Collections.singletonList(id);
        boolean existsByMenuIds = roleMenuRepository.existsByMenuIds(menuIds);
        TiAssert.isTrue(!existsByMenuIds, TiBizErrCode.FAIL, "删除失败,请解绑所有的角色菜单");
        TiAssert.isTrue(menuRepository.remove(id), TiBizErrCode.FAIL, "删除失败");
    }

    public void modify(MenuDTO menuDTO) {
        TiValidUtil.valid(menuDTO, TiValidGroup.Upd.class);
        Menu dbMenu = menuRepository.find(menuDTO.getId());
        TiAssert.isTrue(Objects.equals(dbMenu.getParentId(), menuDTO.getParentId()), TiBizErrCode.FAIL, "父节点必须保持一致");
        TiAssert.isTrue(Objects.equals(dbMenu.getType(), menuDTO.getType()), TiBizErrCode.FAIL, "菜单类型不可更改");
        TiAssert.isNotNull(dbMenu, TiBizErrCode.FAIL, "菜单不存在");
        Menu menu = checkAndGetMenu(menuDTO);
        TiAssert.isTrue(menuRepository.modify(menu), TiBizErrCode.FAIL, "修改失败");
    }

    private Menu checkAndGetMenu(MenuDTO menuDTO) {
        Integer type = menuDTO.getType();
        Long parentId = menuDTO.getParentId();
        boolean isRoot = Objects.equals(CommConst.PARENT_ID, parentId);
        Menu parentMenu = null;
        Integer parentType = 1;
        String parentTypeName = "目录";
        if (!isRoot) {
            parentMenu = menuRepository.find(parentId);
            TiAssert.isNotNull(parentMenu, TiBizErrCode.PARAM_ERROR, "父节点不存在");
            parentType = parentMenu.getType();
            parentTypeName = MenuType.getByCode(parentMenu.getType());
        }
        // 1-目录，父亲一定是目录，目录和路由的路由地址不能重复
        if (Objects.equals(MenuType.DIR.code(), type)) {
            TiValidUtil.valid(menuDTO, MenuDTO.Dir.class);
            TiAssert.isTrue(Objects.equals(parentType, MenuType.DIR.code()), TiBizErrCode.FAIL, StrUtil.format("{}下不能新建目录", parentTypeName));
            Menu getByTypesAndPath = menuRepository.getByTypesAndPath(MenuType.dirOrMenus(), menuDTO.getPath(), menuDTO.getId());
            // 菜单或路由path不能重复
            TiAssert.isNull(getByTypesAndPath, TiBizErrCode.FAIL, "目录路由重复");
            if (!Objects.equals(menuDTO.getExtFlag(), 1)) {
                TiAssert.isNotBlank(menuDTO.getComponentName(), TiBizErrCode.FAIL, "组件名称不能为空");
                Menu repeatCompMenu = menuRepository.getByTypesAndComNameExcludeId(MenuType.dirOrMenus(), menuDTO.getComponentName(), menuDTO.getId());
                // 按钮名称不能重复
                TiAssert.isNull(repeatCompMenu, TiBizErrCode.FAIL, "组件名称重复");
            }
        }
        // 2-菜单，父亲一定是目录，目录和路由的路由地址不能重复
        else if (Objects.equals(MenuType.MENU.code(), type)) {
            TiValidUtil.valid(menuDTO, MenuDTO.Menu.class);
            // 不是外部链接，则组件路径和组件名称不能为空
            if (!Objects.equals(menuDTO.getExtFlag(), 1)) {
                TiValidUtil.valid(menuDTO, MenuDTO.Ext.class);
            }
            if (Objects.equals(menuDTO.getInvisible(), 1)) {
                menuDTO.setCurrentActiveMenu("");
            }
            TiAssert.isTrue(Objects.equals(parentType, MenuType.DIR.code()), TiBizErrCode.FAIL, StrUtil.format("{}下不能新建菜单", parentTypeName));
            Menu getByTypesAndPath = menuRepository.getByTypesAndPath(MenuType.dirOrMenus(), menuDTO.getPath(), menuDTO.getId());
            // 菜单或路由path不能重复
            TiAssert.isNull(getByTypesAndPath, TiBizErrCode.FAIL, "菜单路由重复");
            Menu repeatCompMenu = menuRepository.getByTypesAndComNameExcludeId(MenuType.dirOrMenus(), menuDTO.getComponentName(), menuDTO.getId());
            // 按钮名称不能重复
            TiAssert.isNull(repeatCompMenu, TiBizErrCode.FAIL, "组件名称重复");
        }
        // 3-按钮，父亲一定是菜单, 组件名称不能重复
        else if (Objects.equals(MenuType.BUTTON.code(), type)) {
            TiValidUtil.valid(menuDTO, MenuDTO.Button.class);
            TiAssert.isTrue(Objects.equals(parentType, MenuType.MENU.code()), TiBizErrCode.FAIL, StrUtil.format("{}下不能新建按钮", parentTypeName));
            Menu repeatCompMenu = menuRepository.getByTypesAndComNameExcludeId(Collections.singletonList(MenuType.BUTTON.code()), menuDTO.getComponentName(), menuDTO.getId());
            // 按钮名称不能重复
            TiAssert.isNull(repeatCompMenu, TiBizErrCode.FAIL, "按钮名称重复");
        } else {
            TiAssert.cast(TiBizErrCode.PARAM_ERROR, "未知菜单类型");
        }
        Menu menu = menuAssembler.toEntity(menuDTO);
        fillMenu(menu, parentMenu);
        return menu;
    }

    public MenuDTO getById(Long id) {
        Menu menu = menuRepository.find(id);
        return menuAssembler.toDTO(menu);
    }

    public List<MenuDtlDTO> list() {
        List<Menu> menus = menuRepository.cacheList();
        List<MenuDtlDTO> menuFuncDtls = getMenuDtls(menus, null, null);
        Consumer<MenuDtlDTO> consumer = (root) -> {
            List<MenuDtlDTO> children = root.getChildren();
            children = CollUtil.isEmpty(children) ? null : children;
            root.setChildren(children);
        };
        MenuDtlDTO root = new MenuDtlDTO();
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
            menus = getMenusByRoleCodes(roleCodes, Function.identity()).collect(Collectors.toList());
        }
        List<RouteDTO> routes = menus
            .stream()
            .filter(x -> Objects.equals(1, x.getStatus()))
            .sorted(Comparator.comparing(Menu::getParentId).thenComparing(Comparator.nullsLast(Comparator.comparing(Menu::getSort))))
            .map(this::getEntityToRouteDto)
            .collect(Collectors.toList());
        Consumer<RouteDTO> afterConsumer = (root) -> {
            if (!Objects.equals(root.getType(), 2)) {
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

    private RouteDTO getEntityToRouteDto(Menu menu) {
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
        Integer status = Optional.ofNullable(menu.getStatus()).orElse(1);
        menu.modify(parentId, structure, status);
    }


}
