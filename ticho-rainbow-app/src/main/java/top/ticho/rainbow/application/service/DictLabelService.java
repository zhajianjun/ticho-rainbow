package top.ticho.rainbow.application.service;

import top.ticho.rainbow.interfaces.dto.DictLabelDTO;

import java.util.List;

/**
 * 字典标签 服务接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictLabelService {
    /**
     * 保存字典标签
     *
     * @param dictLabelDTO 字典标签DTO 对象
     */
    void save(DictLabelDTO dictLabelDTO);

    /**
     * 删除字典标签
     *
     * @param id 编号
     */
    void removeById(Long id);

    /**
     * 修改字典标签
     *
     // * @param dictLabelDTO 字典标签DTO 对象
     // */
    void updateById(DictLabelDTO dictLabelDTO);

    /**
     * 根据字典编码查询字典
     *
     * @param code 字典编码
     * @return {@link DictLabelDTO}
     */
    List<DictLabelDTO> getByCode(String code);

}

