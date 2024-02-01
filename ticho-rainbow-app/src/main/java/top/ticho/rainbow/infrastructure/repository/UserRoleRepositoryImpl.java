package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.entity.UserRole;
import top.ticho.rainbow.infrastructure.mapper.UserRoleMapper;

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
@Slf4j
@Service
public class UserRoleRepositoryImpl extends RootServiceImpl<UserRoleMapper, UserRole> implements UserRoleRepository {

    @Override
    @Cacheable(value = CacheConst.USER_ROLE_INFO, key = "#userId")
    public List<Long> listByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);
        return list(wrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConst.USER_ROLE_INFO, key = "#userId")
    public void removeAndSave(Long userId, Collection<Long> roleIds) {
        if (Objects.isNull(userId)) {
            return;
        }
        // @formatter:off
        removeByUserId(userId);
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        List<UserRole> userRoles = roleIds
            .stream()
            .map(x-> convertToUserRole(userId, x))
            .collect(Collectors.toList());
        saveBatch(userRoles);
        // @formatter:on
    }

    private UserRole convertToUserRole(Long userId, Long roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        return userRole;
    }

    @Override
    public boolean existsByRoleIds(Collection<Long> roleIds) {
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.in(UserRole::getRoleId, roleIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    public void removeByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return;
        }
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);
        remove(wrapper);
    }

}
