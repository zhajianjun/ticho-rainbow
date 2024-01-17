package top.ticho.intranet.server.application.service;

import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.dto.DictTypeDTO;

import java.util.List;
import java.util.Map;

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
     * @param id          编号
     */
    void removeById(Long id);

    /**
     * 修改字典
     *
     * @param dictDTO 字典DTO 对象
     */
    void updateById(DictDTO dictDTO);

    /**
     * 根据字典编码查询字典
     *
     * @param code 字典编码
     * @return {@link DictDTO}
     */
    List<DictDTO> getByCode(String code);

    /**
     * 查询所有有效字典
     */
    List<DictTypeDTO> getAllDict();

    /**
     * 刷新所有有效字典
     */
    List<DictTypeDTO> flushAllDict();

}

