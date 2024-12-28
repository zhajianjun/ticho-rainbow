package top.ticho.rainbow.application.system.service;

import top.ticho.boot.view.core.TiPageResult;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.query.DictQuery;

import java.io.IOException;
import java.util.List;

/**
 * 字典 服务接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictService {
    /**
     * 保存字典
     *
     * @param dictDTO 字典DTO 对象
     */
    void save(DictDTO dictDTO);

    /**
     * 删除字典
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改字典
     *
     * @param dictDTO 字典DTO 对象
     */
    void updateById(DictDTO dictDTO);

    /**
     * 根据id查询字典
     *
     * @param id 主键
     * @return {@link DictDTO}
     */
    DictDTO getById(Long id);

    /**
     * 分页查询字典列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link DictDTO}>
     */
    TiPageResult<DictDTO> page(DictQuery query);

    /**
     * 查询所有有效字典
     */
    List<DictDTO> list();

    /**
     * 刷新所有有效字典
     */
    List<DictDTO> flush();

    /**
     * 导出字典信息
     *
     * @param query 查询条件
     */
    void expExcel(DictQuery query) throws IOException;
}

