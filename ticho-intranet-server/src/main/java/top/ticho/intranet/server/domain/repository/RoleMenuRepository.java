package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.RoleMenu;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单关联关系 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface RoleMenuRepository extends RootService<RoleMenu> {

    /**
     * 根据角色id列表查询
     *
     * @param roleIds 角色id
     * @return {@link List}<{@link RoleMenu}>
     */
    List<RoleMenu> listByRoleIds(List<Long> roleIds);

    /**
     * 根据角色id列表查询是否存在
     *
     * @param roleIds 角色id
     * @return boolean
     */
    boolean existsByRoleIds(Collection<Long> roleIds);

    /**
     * 根据菜单id列表查询
     *
     * @param menuIds 菜单id列表
     * @return boolean
     */
    boolean existsByMenuIds(Collection<Long> menuIds);

    /**
     * 根据角色id和菜单id列表删除
     *
     * @param roleId 角色id
     * @param menuIds 菜单id列表
     * @return boolean
     */
    boolean removeByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds);

}

