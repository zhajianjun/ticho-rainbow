package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.application.dto.query.ClientQuery;
import top.ticho.rainbow.application.dto.response.ClientDTO;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-04-13 17:21
 */
public interface ClientAppRepository {

    /**
     * 查询所有客户端信息
     */
    List<ClientDTO> all();

    /**
     * 分页查询客户端信息列表
     */
    TiPageResult<ClientDTO> page(ClientQuery query);

}
