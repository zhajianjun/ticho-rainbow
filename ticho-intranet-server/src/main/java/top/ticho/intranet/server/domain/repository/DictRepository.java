package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.interfaces.query.DictQuery;

import java.util.List;

/**
 * 数据字典 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictRepository extends RootService<Dict> {

    /**
     * 根据条件查询Dict列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Dict}>
     */
    List<Dict> list(DictQuery query);

    /**
     * 根据字典类型编码查询是否存在
     *
     * @param code 字典类型编码
     * @return boolean
     */
    boolean existsByCode(String code);

    /**
     * 根据字典类型编码排除主键编号查询
     *
     * @param code      字典类型编码
     * @param value     字典值
     * @param excludeId 排除的主键编号
     * @return {@link Dict}
     */
    Dict getByCodeAndValueExcludeId(String code, String value, Long excludeId);

}

