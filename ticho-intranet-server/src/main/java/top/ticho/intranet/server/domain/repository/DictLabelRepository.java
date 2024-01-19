package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.DictLabel;
import top.ticho.intranet.server.interfaces.query.DictLabelQuery;

import java.util.List;

/**
 * 字典标签 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictLabelRepository extends RootService<DictLabel> {

    /**
     * 根据条件查询Dict列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link DictLabel}>
     */
    List<DictLabel> list(DictLabelQuery query);

    /**
     * 根据字典编码查询
     *
     * @param code 字典编码
     * @return boolean
     */
    List<DictLabel> getByCode(String code);

    /**
     * 根据字典编码查询是否存在
     *
     * @param code 字典编码
     * @return boolean
     */
    boolean existsByCode(String code);

    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      字典编码
     * @param value     字典值
     * @param excludeId 排除的主键编号
     * @return {@link DictLabel}
     */
    DictLabel getByCodeAndValueExcludeId(String code, String value, Long excludeId);

}

