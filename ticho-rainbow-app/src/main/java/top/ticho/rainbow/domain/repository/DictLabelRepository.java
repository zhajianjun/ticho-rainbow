package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.DictLabel;

import java.util.List;

/**
 * 字典标签 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictLabelRepository {

    boolean save(DictLabel dictLabel);

    boolean remove(Long id);

    boolean modify(DictLabel dictLabel);

    boolean modifyBatch(List<DictLabel> dictLabels);

    DictLabel find(Long id);

    List<DictLabel> list(List<Long> ids);

    /**
     * 查询启用的字典标签信息列表
     */
    List<DictLabel> listEnable();

    /**
     * 根据字典编码查询
     *
     * @param code 字典编码
     */
    List<DictLabel> getByCode(String code);

    /**
     * 根据字典编码查询是否存在
     *
     * @param code 字典编码
     */
    boolean existsByCode(String code);

    /**
     * 根据字典编码排除主键编号查询
     *
     * @param code      字典编码
     * @param value     字典值
     * @param excludeId 排除的主键编号
     */
    DictLabel getByCodeAndValueExcludeId(String code, String value, Long excludeId);

}

