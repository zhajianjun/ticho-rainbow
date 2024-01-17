package top.ticho.intranet.server.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.intranet.server.interfaces.dto.DictTypeDTO;
import top.ticho.intranet.server.interfaces.query.DictTypeQuery;

/**
 * 字典类型 服务接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictTypeService {
    /**
     * 保存字典类型
     *
     * @param dictTypeDTO 字典类型DTO 对象
     */
    void save(DictTypeDTO dictTypeDTO);

    /**
     * 删除字典类型
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改字典类型
     *
     * @param dictTypeDTO 字典类型DTO 对象
     */
    void updateById(DictTypeDTO dictTypeDTO);

    /**
     * 根据id查询字典类型
     *
     * @param id 主键
     * @return {@link DictTypeDTO}
     */
    DictTypeDTO getById(Long id);

    /**
     * 分页查询字典类型列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link DictTypeDTO}>
     */
    PageResult<DictTypeDTO> page(DictTypeQuery query);

}

