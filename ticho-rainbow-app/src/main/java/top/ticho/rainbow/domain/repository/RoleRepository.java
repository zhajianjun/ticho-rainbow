package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.application.dto.query.RoleQuery;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.entity.Role;

import java.util.List;

/**
 * 角色信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface RoleRepository {

    /**
     * 保存角色
     *
     * @param role 角色
     * @return boolean
     */
    boolean save(Role role);

    /**
     * 删除角色
     *
     * @param id 身份证
     * @return boolean
     */
    boolean remove(Long id);

    /**
     * 修改角色
     *
     * @param role 角色
     */
    boolean modify(Role role);

    /**
     * 根据编号查询角色
     *
     * @param id 编号
     * @return {@link Role }
     */
    Role find(Long id);

    /**
     * 所有角色缓存查询
     *
     * @return {@link List}<{@link Role}>
     */
    List<Role> cacheList();

    /**
     * 根据条件查询Role列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Role}>
     */
    List<Role> list(RoleQuery query);

    /**
     * 根据角色code查询Role列表
     *
     * @param codes 角色代码
     * @return {@link List}<{@link Role}>
     */
    List<Role> listByCodes(List<String> codes);

    /**
     * 获取游客角色
     *
     * @return {@link Role}
     */
    Role getGuestRole();


    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      角色编码
     * @param excludeId 排除的主键编号
     * @return {@link DictLabel}
     */
    Role getByCodeExcludeId(String code, Long excludeId);

    List<Role> listByIds(List<Long> ids);

}

