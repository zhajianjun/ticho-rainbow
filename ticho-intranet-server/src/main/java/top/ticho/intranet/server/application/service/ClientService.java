package top.ticho.intranet.server.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.intranet.server.interfaces.dto.ClientDTO;
import top.ticho.intranet.server.interfaces.query.ClientQuery;
import top.ticho.tool.intranet.server.entity.ClientInfo;

import java.util.List;

/**
 * 客户端信息 服务接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface ClientService {
    /**
     * 保存客户端信息
     *
     * @param clientDTO 客户端信息DTO 对象
     */
    void save(ClientDTO clientDTO);

    /**
     * 删除客户端信息
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改客户端信息
     *
     * @param clientDTO 客户端信息DTO 对象
     */
    void updateById(ClientDTO clientDTO);

    /**
     * 根据id查询客户端信息
     *
     * @param id 主键
     * @return {@link ClientDTO}
     */
    ClientDTO getById(Long id);

    /**
     * 分页查询客户端信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link ClientDTO}>
     */
    PageResult<ClientDTO> page(ClientQuery query);

    /**
     * 查询客户端信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link ClientDTO}>
     */
    List<ClientDTO> list(ClientQuery query);

    /**
     * 查询所有有效客户端通道信息
     *
     * @return {@link List}<{@link ClientInfo}>
     */
    List<ClientInfo> listClientInfo();

}

