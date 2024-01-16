package top.ticho.intranet.server.application.service;

import top.ticho.intranet.server.interfaces.dto.DictDTO;

import java.util.List;
import java.util.Map;

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
     * 根据字典编码查询数据字典
     *
     * @param code 字典编码
     * @return {@link DictDTO}
     */
    List<DictDTO> getByCode(String code);

    /**
     * 查询所有有效字典
     */
    Map<String, Map<String, String>> getAllDict();

    /**
     * 刷新所有有效字典
     */
    void flushAllDict();

}

