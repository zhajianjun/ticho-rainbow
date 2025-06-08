package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.entity.DictLabel;

import java.util.List;

/**
 * 字典 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictRepository {

    boolean save(Dict dict);

    boolean remove(Long id);

    boolean modify(Dict dict);

    boolean modifyBatch(List<Dict> roles);

    Dict find(Long id);

    List<Dict> list(List<Long> ids);

    /**
     * 查询启用的字典列表
     *
     * @return {@link List}<{@link Dict}>
     */
    List<Dict> listEnable();

    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      字典编码
     * @param excludeId 排除的主键编号
     * @return {@link DictLabel}
     */
    Dict getByCodeExcludeId(String code, Long excludeId);

}

