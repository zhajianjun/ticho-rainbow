package top.ticho.intranet.server.domain.repository;

import com.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Client;
import top.ticho.intranet.server.interfaces.query.ClientQuery;

import java.util.List;

/**
 * 客户端信息 repository接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface ClientRepository extends RootService<Client> {

    /**
     * 根据条件查询客户端信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Client}>
     */
    List<Client> list(ClientQuery query);

}

