package top.ticho.rainbow.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.domain.entity.UserRole;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.mapper.UserRoleMapper;
import top.ticho.rainbow.infrastructure.persistence.po.UserRolePO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.web.util.TiSpringUtil;
import top.ticho.tool.core.TiCollUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户角色关联关系 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Repository
public class UserRoleRepositoryImpl extends TiRepositoryImpl<UserRoleMapper, UserRolePO> implements UserRoleRepository {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<UserRole> userRoles) {
        userRoles
            .stream()
            .collect(Collectors.groupingBy(UserRole::getUserId, Collectors.mapping(UserRole::getRoleId, Collectors.toList())))
            .forEach(this::saveUserRoles);
    }

    @Override
    @Cacheable(value = CacheConst.USER_ROLE_INFO, key = "#userId")
    public List<Long> listByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserRolePO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRolePO::getUserId, userId);
        return list(wrapper).stream().map(UserRolePO::getRoleId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeAndSave(Long userId, Collection<Long> roleIds) {
        if (Objects.isNull(userId)) {
            return;
        }
        UserRoleRepositoryImpl bean = TiSpringUtil.getBean(this.getClass());
        bean.removeByUserId(userId);
        if (TiCollUtil.isEmpty(roleIds)) {
            return;
        }
        saveUserRoles(userId, roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Long userId, Collection<Long> roleIds) {
        if (Objects.isNull(userId)) {
            return;
        }
        if (TiCollUtil.isEmpty(roleIds)) {
            return;
        }
        List<UserRolePO> userRolePOS = roleIds
            .stream()
            .map(x -> convertToUserRole(userId, x))
            .collect(Collectors.toList());
        super.saveBatch(userRolePOS);
    }

    @Override
    public boolean existsByRoleIds(Collection<Long> roleIds) {
        LambdaQueryWrapper<UserRolePO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(UserRolePO::getRoleId, roleIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @CacheEvict(value = CacheConst.USER_ROLE_INFO, key = "#userId")
    public boolean removeByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return false;
        }
        LambdaQueryWrapper<UserRolePO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRolePO::getUserId, userId);
        return remove(wrapper);
    }

    private UserRolePO convertToUserRole(Long userId, Long roleId) {
        UserRolePO userRolePO = new UserRolePO();
        userRolePO.setUserId(userId);
        userRolePO.setRoleId(roleId);
        return userRolePO;
    }

}
