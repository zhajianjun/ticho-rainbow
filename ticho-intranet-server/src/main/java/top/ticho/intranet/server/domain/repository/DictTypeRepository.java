package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.infrastructure.entity.DictType;
import top.ticho.intranet.server.interfaces.query.DictTypeQuery;

import java.util.List;

/**
 * 数据字典类型 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictTypeRepository extends RootService<DictType> {

    /**
     * 根据条件查询DictType列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link DictType}>
     */
    List<DictType> list(DictTypeQuery query);

    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      字典编码
     * @param excludeId 排除的主键编号
     * @return {@link Dict}
     */
    DictType getByCodeExcludeId(String code, Long excludeId);

}

