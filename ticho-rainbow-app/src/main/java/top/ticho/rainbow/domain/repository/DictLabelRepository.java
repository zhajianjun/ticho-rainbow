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

    /**
     * 保存字典标签
     *
     * @param dictLabel 字典标签
     * @return boolean
     */
    boolean save(DictLabel dictLabel);

    /**
     * 删除字典标签
     *
     * @param id 编号
     * @return boolean
     */
    boolean remove(Long id);

    /**
     * 修改字典标签
     *
     * @param dictLabel 字典标签信息
     * @return boolean
     */
    boolean modify(DictLabel dictLabel);

    /**
     * 根据编号查询字典标签信息
     *
     * @param id 编号
     * @return {@link DictLabel }
     */
    DictLabel find(Long id);

    /**
     * 查询启用的字典标签信息列表
     *
     * @return {@link List}<{@link DictLabel}>
     */
    List<DictLabel> listEnable();

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

