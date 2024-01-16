package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.RoleMenuRepository;
import top.ticho.intranet.server.infrastructure.core.constant.CacheConst;
import top.ticho.intranet.server.infrastructure.entity.RoleMenu;
import top.ticho.intranet.server.infrastructure.mapper.RoleMenuMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色菜单关联关系 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class RoleMenuRepositoryImpl extends RootServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuRepository {

    @Override
    @Cacheable(value = CacheConst.ROLE_MENU_INFO, key = "#roleId")
    public List<Long> listByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        return list(wrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public boolean existsByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenu::getRoleId, roleIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    public boolean existsByMenuIds(Collection<Long> menuIds) {
        if (CollUtil.isEmpty(menuIds)) {
            return false;
        }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenu::getMenuId, menuIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConst.ROLE_MENU_INFO, key = "#roleId")
    public void removeAndSave(Long roleId, Collection<Long> menuIds) {
        // @formatter:off
        if (Objects.isNull(roleId)) {
            return;
        }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        remove(wrapper);
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }
        // 需要添加角色用户关联关系
        List<RoleMenu> roleMenus = menuIds
            .stream()
            .map(x -> convertToRoleMenu(roleId, x))
            .collect(Collectors.toList());
        saveBatch(roleMenus);
        // @formatter:on
    }

    private RoleMenu convertToRoleMenu(Long roleId, Long x) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(x);
        return roleMenu;
    }


}
