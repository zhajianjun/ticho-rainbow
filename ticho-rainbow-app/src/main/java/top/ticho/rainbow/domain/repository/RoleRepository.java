package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Role;

import java.util.List;

/**
 * 角色信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface RoleRepository {

    boolean save(Role role);

    boolean remove(Long id);

    boolean modify(Role role);

    boolean modifyBatch(List<Role> roles);

    Role find(Long id);

    List<Role> list(List<Long> ids);

    /**
     * 所有角色缓存查询
     */
    List<Role> cacheList();

    /**
     * 根据角色code查询Role列表
     */
    List<Role> listByCodes(List<String> codes);

    /**
     * 获取游客角色
     */
    Role getGuestRole();


    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      角色编码
     * @param excludeId 排除的主键编号
     */
    Role getByCodeExcludeId(String code, Long excludeId);

}

