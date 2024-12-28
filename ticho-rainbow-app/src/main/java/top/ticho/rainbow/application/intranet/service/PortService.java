package top.ticho.rainbow.application.intranet.service;

import top.ticho.boot.view.core.TiPageResult;
import top.ticho.rainbow.interfaces.dto.PortDTO;
import top.ticho.rainbow.interfaces.query.PortQuery;

import java.io.IOException;

/**
 * 端口信息 服务接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface PortService {
    /**
     * 保存端口信息
     *
     * @param portDTO 端口信息DTO 对象
     */
    void save(PortDTO portDTO);

    /**
     * 删除端口信息
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改端口信息
     *
     * @param portDTO 端口信息DTO 对象
     */
    void updateById(PortDTO portDTO);

    /**
     * 根据id查询端口信息
     *
     * @param id 主键
     * @return {@link PortDTO}
     */
    PortDTO getById(Long id);

    /**
     * 分页查询端口信息列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link PortDTO}>
     */
    TiPageResult<PortDTO> page(PortQuery query);

    /**
     * 导出端口信息
     *
     * @param query 查询条件
     */
    void expExcel(PortQuery query) throws IOException;
}

