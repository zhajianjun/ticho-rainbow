package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.UserRole;

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
     * 通过用户id查询
     *
     * @param userId 用户id
     * @return {@link List}<{@link UserRole}>
     */
    List<UserRole> listByUserId(Long userId);

    /**
     * 通过用户id列表查询
     *
     * @param userIds 用户id列表
     * @return {@link List}<{@link UserRole}>
     */
    List<UserRole> listByUserIds(Collection<Long> userIds);

    /**
     * 根据角色id列表查询是否存在
     *
     * @param roleIds 角色id
     * @return boolean
     */
    boolean existsByRoleIds(Collection<Long> roleIds);

    /**
     * 通过用户id删除
     *
     * @param userId 用户id
     * @return boolean
     */
    boolean removeByUserId(Long userId);

}

