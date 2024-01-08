package top.ticho.intranet.server.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.query.DictQuery;

/**
 * 数据字典 服务接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictService {
    /**
     * 保存数据字典
     *
     * @param dictDTO 数据字典DTO 对象
     */
    void save(DictDTO dictDTO);

    /**
     * 删除数据字典
     *
     * @param id          编号
     * @param isDelChilds 是否删除其子节点
     */
    void removeById(Long id, Boolean isDelChilds);

    /**
     * 修改数据字典
     *
     * @param dictDTO 数据字典DTO 对象
     */
    void updateById(DictDTO dictDTO);

    /**
     * 根据id查询数据字典
     *
     * @param id 主键
     * @return {@link DictDTO}
     */
    DictDTO getById(Long id);

    /**
     * 分页查询数据字典列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link DictDTO}>
     */
    PageResult<DictDTO> page(DictQuery query);

}

