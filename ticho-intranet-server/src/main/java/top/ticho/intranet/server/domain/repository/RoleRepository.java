package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.infrastructure.entity.Role;
import top.ticho.intranet.server.interfaces.query.RoleQuery;

import java.util.List;

/**
 * 角色信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface RoleRepository extends RootService<Role> {

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
     * 根据字典类型编码排除主键编号查询
     *
     * @param code      角色编码
     * @param excludeId 排除的主键编号
     * @return {@link Dict}
     */
    Role getByCodeExcludeId(String code, Long excludeId);

}

