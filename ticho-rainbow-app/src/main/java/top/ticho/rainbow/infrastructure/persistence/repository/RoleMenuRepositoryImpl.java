package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.mapper.RoleMenuMapper;
import top.ticho.rainbow.infrastructure.persistence.po.RoleMenuPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;

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
@Service
@RequiredArgsConstructor
public class RoleMenuRepositoryImpl extends TiRepositoryImpl<RoleMenuMapper, RoleMenuPO> implements RoleMenuRepository {

    @Override
    @Cacheable(value = CacheConst.ROLE_MENU_INFO, key = "#roleId")
    public List<Long> listByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RoleMenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenuPO::getRoleId, roleId);
        return list(wrapper).stream().map(RoleMenuPO::getMenuId).collect(Collectors.toList());
    }

    @Override
    public boolean existsByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        LambdaQueryWrapper<RoleMenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenuPO::getRoleId, roleIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    public boolean existsByMenuIds(Collection<Long> menuIds) {
        if (CollUtil.isEmpty(menuIds)) {
            return false;
        }
        LambdaQueryWrapper<RoleMenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenuPO::getMenuId, menuIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConst.ROLE_MENU_INFO, key = "#roleId")
    public void removeAndSave(Long roleId, Collection<Long> menuIds) {
        if (Objects.isNull(roleId)) {
            return;
        }
        LambdaQueryWrapper<RoleMenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenuPO::getRoleId, roleId);
        remove(wrapper);
        if (CollUtil.isEmpty(menuIds)) {
            return;
        }
        // 需要添加角色用户关联关系
        List<RoleMenuPO> roleMenuPOS = menuIds
            .stream()
            .map(x -> convertToRoleMenu(roleId, x))
            .collect(Collectors.toList());
        saveBatch(roleMenuPOS);
    }

    private RoleMenuPO convertToRoleMenu(Long roleId, Long x) {
        RoleMenuPO roleMenuPO = new RoleMenuPO();
        roleMenuPO.setRoleId(roleId);
        roleMenuPO.setMenuId(x);
        return roleMenuPO;
    }


}
