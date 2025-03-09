package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.RoleMenu;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单关联关系 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface RoleMenuRepository {

    /**
     * 根据角色id列表查询
     *
     * @param roleId 角色id
     * @return {@link List}<{@link RoleMenu}>
     */
    List<Long> listByRoleId(Long roleId);

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
     * 移除角色菜单信息，并重新保存菜单
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    void removeAndSave(Long roleId, Collection<Long> menuIds);

}

