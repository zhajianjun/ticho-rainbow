package top.ticho.intranet.server.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.TreeUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.MenuService;
import top.ticho.intranet.server.domain.handle.UpmsHandle;
import top.ticho.intranet.server.domain.repository.MenuRepository;
import top.ticho.intranet.server.domain.repository.RoleMenuRepository;
import top.ticho.intranet.server.infrastructure.core.constant.CommConst;
import top.ticho.intranet.server.infrastructure.core.constant.SecurityConst;
import top.ticho.intranet.server.infrastructure.core.enums.MenuType;
import top.ticho.intranet.server.infrastructure.core.util.UserUtil;
import top.ticho.intranet.server.infrastructure.entity.Menu;
import top.ticho.intranet.server.interfaces.assembler.MenuAssembler;
import top.ticho.intranet.server.interfaces.dto.MenuDTO;
import top.ticho.intranet.server.interfaces.dto.MenuDtlDTO;
import top.ticho.intranet.server.interfaces.dto.RouteDTO;
import top.ticho.intranet.server.interfaces.dto.RouteMetaDTO;
import top.ticho.intranet.server.interfaces.dto.SecurityUser;

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
public class MenuServiceImpl extends UpmsHandle implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Override
    public void save(MenuDTO menuDTO) {
        ValidUtil.valid(menuDTO, ValidGroup.Add.class);
        menuDTO.setId(CloudIdUtil.getId());
        Menu menu = checkAndGetMenu(menuDTO);
        Assert.isTrue(menuRepository.save(menu), BizErrCode.FAIL, "保存失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        // 子节点为空才能删除
        List<Long> menuIds = Collections.singletonList(id);
        boolean existsByMenuIds = roleMenuRepository.existsByMenuIds(menuIds);
        Assert.isTrue(!existsByMenuIds, BizErrCode.FAIL, "删除失败,请解绑所有的角色菜单");
        Assert.isTrue(menuRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(MenuDTO menuDTO) {
        ValidUtil.valid(menuDTO, ValidGroup.Upd.class);
        Menu dbMenu = menuRepository.getById(menuDTO.getId());
        Assert.isTrue(Objects.equals(dbMenu.getParentId(), menuDTO.getParentId()), BizErrCode.FAIL, "父节点必须保持一致" );
        Assert.isTrue(Objects.equals(dbMenu.getType(), menuDTO.getType()), BizErrCode.FAIL, "菜单类型不可更改" );
        Assert.isNotNull(dbMenu, BizErrCode.FAIL, "菜单不存在");
        Menu menu = checkAndGetMenu(menuDTO);
        Assert.isTrue(menuRepository.updateById(menu), BizErrCode.FAIL, "修改失败");
    }

    private Menu checkAndGetMenu(MenuDTO menuDTO) {
        Integer type = menuDTO.getType();
        Long parentId = menuDTO.getParentId();
        boolean isRoot = Objects.equals(CommConst.PARENT_ID, parentId);
        Menu parentMenu = null;
        Integer parentType = 1;
        String parentTypeName = "目录";
        if (!isRoot) {
            parentMenu = menuRepository.getById(parentId);
            Assert.isNotNull(parentMenu, BizErrCode.PARAM_ERROR, "父节点不存在");
            parentType = parentMenu.getType();
            parentTypeName = MenuType.getByCode(parentMenu.getType());
        }
        // 1-目录，父亲一定是目录，目录和路由的路由地址不能重复
        if (Objects.equals(MenuType.DIR.code(), type)) {
            ValidUtil.valid(menuDTO, MenuDTO.Dir.class);
            Assert.isTrue(Objects.equals(parentType, MenuType.DIR.code()), BizErrCode.FAIL, StrUtil.format("{}下不能新建目录", parentTypeName));
            long countChildPath = menuRepository.countByTypesAndPath(MenuType.dirOrMenus(), menuDTO.getPath(), menuDTO.getId());
            // 菜单或路由path不能重复
            Assert.isTrue(countChildPath == 0, BizErrCode.FAIL, "目录路由重复");
        }
        // 2-菜单，父亲一定是目录，目录和路由的路由地址不能重复
        else if (Objects.equals(MenuType.MENU.code(), type)) {
            ValidUtil.valid(menuDTO, MenuDTO.Menu.class);
            // 不是外部链接，则组件路径和组件名称不能为空
            if (!Objects.equals(menuDTO.getExtFlag(), 1)) {
                ValidUtil.valid(menuDTO, MenuDTO.Ext.class);
            }
            Assert.isTrue(Objects.equals(parentType, MenuType.DIR.code()), BizErrCode.FAIL, StrUtil.format("{}下不能新建菜单", parentTypeName));
            long countChildPath = menuRepository.countByTypesAndPath(MenuType.dirOrMenus(), menuDTO.getPath(), menuDTO.getId());
            // 菜单或路由path不能重复
            Assert.isTrue(countChildPath == 0, BizErrCode.FAIL, "菜单路由重复");
        }
        // 3-按钮，父亲一定是菜单, 组件名称不能重复
        else if (Objects.equals(MenuType.BUTTON.code(), type)) {
            ValidUtil.valid(menuDTO, MenuDTO.Button.class);
            Assert.isTrue(Objects.equals(parentType, MenuType.MENU.code()), BizErrCode.FAIL, StrUtil.format("{}下不能新建按钮", parentTypeName));
            long countChildPath = menuRepository.countByTypeAndComName(MenuType.BUTTON.code(), menuDTO.getComponentName(), menuDTO.getId());
            // 兄弟按钮组件名称不能重复
            Assert.isTrue(countChildPath == 0, BizErrCode.FAIL, "按钮重复");
        } else {
            Assert.cast(BizErrCode.PARAM_ERROR, "未知菜单类型");
        }
        Menu menu = MenuAssembler.INSTANCE.dtoToEntity(menuDTO);
        fillMenu(menu, parentMenu);
        return menu;
    }

    @Override
    public MenuDTO getById(Long id) {
        Menu menu = menuRepository.getById(id);
        return MenuAssembler.INSTANCE.entityToDto(menu);
    }

    @Override
    public List<MenuDtlDTO> list() {
        List<Menu> menus = menuRepository.list();
        List<MenuDtlDTO> menuFuncDtls = getMenuDtls(menus, null, null);
        Consumer<MenuDtlDTO> consumer = (root) -> {
            List<MenuDtlDTO> children = root.getChildren();
            children = CollUtil.isEmpty(children) ? null : children;
            root.setChildren(children);
        };
        MenuDtlDTO root = new MenuDtlDTO();
        root.setId(0L);
        TreeUtil.tree(menuFuncDtls, root, null, consumer);
        List<MenuDtlDTO> result = root.getChildren();
        if (CollUtil.isNotEmpty(result)) {
            result.get(0).setParentId(null);
        }
        return result;
    }

    @Override
    public List<RouteDTO> route() {
        // @formatter:
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
            menus = menuRepository.list();
        } else {
            menus = getMenusByRoleCodes(roleCodes, Function.identity()).collect(Collectors.toList());
        }
        List<RouteDTO> routes = menus
            .stream()
            .filter(x-> Objects.equals(1, x.getStatus()))
            .sorted(Comparator.comparing(Menu::getParentId).thenComparing(Comparator.nullsLast(Comparator.comparing(Menu::getSort))))
            .map(this::getEntityToRouteDto)
            .collect(Collectors.toList());
        Consumer<RouteDTO> consumer = (root) -> {
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
        TreeUtil.tree(routes, root, null, consumer);
        return root.getChildren();
        // @formatter:on
    }

    private RouteDTO getEntityToRouteDto(Menu menu) {
        boolean ignoreKeepAlive = !Objects.equals(menu.getKeepAlive(), 1);
        boolean hideMenu = !Objects.equals(menu.getInvisible(), 1);
        boolean extFlag = Objects.equals(menu.getExtFlag(), 1);
        RouteDTO routeDTO = MenuAssembler.INSTANCE.entityToRouteDto(menu);
        RouteMetaDTO routeMetaDTO = MenuAssembler.INSTANCE.entityToRouteMetaDto(menu);
        routeMetaDTO.setIgnoreKeepAlive(ignoreKeepAlive);
        routeMetaDTO.setHideMenu(hideMenu);
        if (extFlag) {
            routeMetaDTO.setFrameSrc(menu.getRedirect());
        }
        routeDTO.setMeta(routeMetaDTO);
        return routeDTO;
    }

    private void fillMenu(Menu menu, Menu parent) {
        // 父节点不存在则该节点为父节点
        if (Objects.isNull(parent)) {
            menu.setStructure(String.valueOf(menu.getId()));
            menu.setParentId(CommConst.PARENT_ID);
        } else {
            menu.setStructure(parent.getStructure() + "-" + menu.getId());
            menu.setParentId(parent.getId());
        }
        Integer status = Optional.ofNullable(menu.getStatus()).orElse(1);
        menu.setStatus(status);
        menu.setIsDelete(0);
    }


}
