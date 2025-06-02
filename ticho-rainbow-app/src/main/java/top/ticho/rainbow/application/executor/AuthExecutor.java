package top.ticho.rainbow.application.executor;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.assembler.MenuAssembler;
import top.ticho.rainbow.application.assembler.RoleAssembler;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.application.dto.response.RoleMenuDtlDTO;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.repository.MenuRepository;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.infrastructure.common.enums.MenuType;
import top.ticho.starter.web.util.TiTreeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 权限服务抽象类
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@RequiredArgsConstructor
@Component
public class AuthExecutor {
    private final RoleRepository roleRepository;
    private final RoleAssembler roleAssembler;
    private final RoleMenuRepository roleMenuRepository;
    private final MenuRepository menuRepository;
    private final MenuAssembler menuAssembler;

    /**
     * 获取状态正常的菜单流
     *
     * @param roleCodes 角色编码
     * @param map       转换
     * @return {@link Stream}<{@link T}>
     */
    public <T> Stream<T> getMenusByRoleCodes(List<String> roleCodes, Function<Menu, T> map) {
        if (CollUtil.isEmpty(roleCodes)) {
            return Stream.empty();
        }
        // 根据角色id列表 查询角色信息
        List<Role> roles = roleRepository.listByCodes(roleCodes);
        List<Long> roleIds = roles
            .stream()
            .filter(x -> Objects.equals(1, x.getStatus()))
            .map(Role::getId)
            .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleIds)) {
            return Stream.empty();
        }
        // 合并的角色后所有的菜单
        List<Long> menuIds = roleIds
            .stream()
            .map(roleMenuRepository::listByRoleId)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        if (CollUtil.isEmpty(menuIds)) {
            return Stream.empty();
        }
        // 菜单信息
        List<Menu> menus = menuRepository.cacheList();
        return menus
            .stream()
            .filter(x -> menuIds.contains(x.getId()))
            .filter(x -> Objects.equals(1, x.getStatus()))
            .map(map)
            .filter(Objects::nonNull);
    }

    public List<String> getPerms(List<String> roleCodes) {
        if (CollUtil.isEmpty(roleCodes)) {
            return Collections.emptyList();
        }
        Function<Menu, String> identity = menu -> {
            if (!Objects.equals(menu.getStatus(), 1) || !Objects.equals(menu.getType(), MenuType.BUTTON.code())) {
                return null;
            }
            return menu.getPerms();
        };
        return getMenusByRoleCodes(roleCodes, identity)
            .map(x -> x.split(","))
            .flatMap(Arrays::stream)
            .collect(Collectors.toList());
    }

    /**
     * 合并菜单按角色id
     *
     * @param roleIds    角色id列表
     * @param showAll    显示所有信息，匹配到的信息，设置匹配字段checkbox=true
     * @param treeHandle 是否进行树化
     * @return {@link RoleMenuDtlDTO}
     */
    public RoleMenuDtlDTO mergeRoleByIds(List<Long> roleIds, boolean showAll, boolean treeHandle) {
        if (CollUtil.isEmpty(roleIds)) {
            return getRoleMenuDtl(Collections.emptyList(), showAll, treeHandle);
        }
        // 1.根据角色id列表查询角色信息、菜单信息、角色菜单信息、角色权限标识信息、菜单权限标识信息
        // 根据角色id列表 查询角色信息
        List<Role> roles = roleRepository.list(roleIds);
        return getRoleMenuDtl(roles, showAll, treeHandle);
    }

    private RoleMenuDtlDTO getRoleMenuDtl(List<Role> roles, boolean showAll, boolean treeHandle) {
        RoleMenuDtlDTO roleMenuDtlDTO = new RoleMenuDtlDTO();
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        roleMenuDtlDTO.setRoleIds(roleIds);
        // 合并的角色后所有的菜单
        List<Long> menuIds = roleIds
            .stream()
            .map(roleMenuRepository::listByRoleId)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        // 菜单信息
        List<Menu> menus = menuRepository.cacheList();
        // 查询到的角色信息组装填充
        List<RoleDTO> roleDtos = new ArrayList<>();
        roleIds = new ArrayList<>();
        List<String> roleCodes = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = roleAssembler.toDTO(role);
            roleIds.add(role.getId());
            roleCodes.add(role.getCode());
            roleDtos.add(roleDTO);
        }
        roleMenuDtlDTO.setRoleIds(roleIds);
        roleMenuDtlDTO.setRoleCodes(roleCodes);
        roleMenuDtlDTO.setRoles(roleDtos);
        List<String> perms = new ArrayList<>();
        // 菜单信息过滤规则
        Predicate<MenuDtlDTO> menuFilter = x -> menuIds.contains(x.getId());
        // 菜单信息操作
        Consumer<MenuDtlDTO> menuPeek = x -> {
            x.setCheckbox(true);
            perms.addAll(x.getPerms());
        };
        // 如果展示全部字段，匹配的数据进行填充checkbox=true
        if (showAll) {
            menuFilter = null;
            menuPeek = x -> {
                perms.addAll(x.getPerms());
                x.setCheckbox(menuIds.contains(x.getId()));
            };
        }
        // 根据菜单信息，填充权限标识信息
        List<MenuDtlDTO> menuFuncDtls = getMenuDtls(menus, menuFilter, menuPeek);
        roleMenuDtlDTO.setMenus(menuFuncDtls);
        if (!treeHandle) {
            return roleMenuDtlDTO;
        }
        // 菜单信息规整为树结构
        MenuDtlDTO root = new MenuDtlDTO();
        root.setId(0L);
        TiTreeUtil.tree(menuFuncDtls, root);
        roleMenuDtlDTO.setMenus(root.getChildren());
        roleMenuDtlDTO.setMenuIds(menuIds);
        roleMenuDtlDTO.setPerms(perms);
        return roleMenuDtlDTO;
    }


    /**
     * 菜单信息转换、过滤、执行规则信息
     *
     * @param menus  菜单
     * @param filter 过滤规则
     * @param peek   执行规则
     * @return {@link List}<{@link MenuDtlDTO}>
     */
    public List<MenuDtlDTO> getMenuDtls(List<Menu> menus, Predicate<MenuDtlDTO> filter, Consumer<MenuDtlDTO> peek) {
        if (filter == null) {
            filter = x -> true;
        }
        if (peek == null) {
            peek = x -> {
            };
        }
        return menus
            .stream()
            .map(menuAssembler::toDtlDTO)
            .filter(filter)
            .peek(peek)
            .sorted(Comparator.comparing(MenuDtlDTO::getParentId).thenComparing(Comparator.nullsLast(Comparator.comparing(MenuDtlDTO::getSort))))
            .collect(Collectors.toList());
    }


}
