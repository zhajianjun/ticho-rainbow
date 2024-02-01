package top.ticho.rainbow.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.rainbow.infrastructure.entity.Dict;
import top.ticho.rainbow.infrastructure.entity.DictLabel;
import top.ticho.rainbow.interfaces.query.DictQuery;

import java.util.List;

/**
 * 字典 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictRepository extends RootService<Dict> {

    /**
     * 根据条件查询DictType列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Dict}>
     */
    List<Dict> list(DictQuery query);

    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      字典编码
     * @param excludeId 排除的主键编号
     * @return {@link DictLabel}
     */
    Dict getByCodeExcludeId(String code, Long excludeId);

}

