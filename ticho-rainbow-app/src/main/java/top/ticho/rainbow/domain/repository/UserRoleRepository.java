package top.ticho.rainbow.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.rainbow.infrastructure.entity.UserRole;

import java.util.Collection;
import java.util.List;

/**
 * 用户角色关联关系 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface UserRoleRepository extends RootService<UserRole> {

    /**
     * 通过用户id查询角色id
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserRole}>
     */
    List<Long> listByUserId(Long userId);

    /**
     * 移除用户角色信息，并重新保存角色id
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    void removeAndSave(Long userId, Collection<Long> roleIds);

    /**
     * 根据角色id列表查询是否存在
     *
     * @param roleIds 角色id
     * @return boolean
     */
    boolean existsByRoleIds(Collection<Long> roleIds);

}

