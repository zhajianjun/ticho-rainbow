package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import top.ticho.rainbow.application.assembler.MenuAssembler;
import top.ticho.rainbow.application.assembler.RoleAssembler;
import top.ticho.rainbow.application.assembler.UserAssembler;
import top.ticho.rainbow.application.dto.RoleDTO;
import top.ticho.rainbow.application.dto.RoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.UserDTO;
import top.ticho.rainbow.application.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.domain.repository.MenuRepository;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.enums.MenuType;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.starter.web.util.TiTreeUtil;

import javax.annotation.Resource;
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
public abstract class AbstractAuthServiceImpl {
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserAssembler userAssembler;
    @Resource
    private UserRoleRepository userRoleRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private RoleAssembler roleAssembler;
    @Resource
    private RoleMenuRepository roleMenuRepository;
    @Resource
    private MenuRepository menuRepository;
    @Resource
    private MenuAssembler menuAssembler;
    @Resource
    private FileInfoRepository fileInfoRepository;
    @Resource
    private FileInfoService fileInfoService;

    public UserDTO getUser(String username) {
        User user = userRepository.getCacheByUsername(username);
        return userAssembler.toDTO(user);
    }

    /**
     * 查询用户角色菜单权限标识信息
     *
     * @param username 用户名
     * @return {@link UserRoleMenuDtlDTO}
     */
    public UserRoleMenuDtlDTO getUserDtl(String username) {
        User user = userRepository.getCacheByUsername(username);
        UserRoleMenuDtlDTO userRoleMenuDtlDTO = userAssembler.toDtlDTO(user);
        if (userRoleMenuDtlDTO == null) {
            return null;
        }
        setPhoto(userRoleMenuDtlDTO);
        List<Long> roleIds = userRoleRepository.listByUserId(user.getId());
        RoleMenuDtlDTO roleMenuFuncDtl = mergeRoleByIds(roleIds, false, false);
        if (roleMenuFuncDtl == null) {
            return null;
        }
        userRoleMenuDtlDTO.setRoleIds(roleMenuFuncDtl.getRoleIds());
        userRoleMenuDtlDTO.setRoleCodes(roleMenuFuncDtl.getRoleCodes());
        userRoleMenuDtlDTO.setMenuIds(roleMenuFuncDtl.getMenuIds());
        userRoleMenuDtlDTO.setRoles(roleMenuFuncDtl.getRoles());
        userRoleMenuDtlDTO.setMenus(roleMenuFuncDtl.getMenus());
        return userRoleMenuDtlDTO;
    }

    private void setPhoto(UserRoleMenuDtlDTO userRoleMenuDtlDTO) {
        if (userRoleMenuDtlDTO == null || !StrUtil.isNumeric(userRoleMenuDtlDTO.getPhoto())) {
            return;
        }
        Long fileId = Long.parseLong(userRoleMenuDtlDTO.getPhoto());
        FileInfo fileInfo = fileInfoRepository.find(fileId);
        if (fileInfo == null) {
            userRoleMenuDtlDTO.setPhoto(null);
            return;
        }
        String url = fileInfoService.getUrl(fileInfo, null, true);
        userRoleMenuDtlDTO.setPhoto(url);
    }

    /**
     * 查询登录人用户角色菜单权限标识信息
     *
     * @return {@link UserRoleMenuDtlDTO}
     */
    public UserRoleMenuDtlDTO getUserDtl() {
        return getUserDtl(UserUtil.getCurrentUsername());
    }

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
        List<Role> roles = roleRepository.listByIds(roleIds);
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
