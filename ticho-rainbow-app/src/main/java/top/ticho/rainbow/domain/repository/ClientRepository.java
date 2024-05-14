package top.ticho.rainbow.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.rainbow.infrastructure.entity.Client;
import top.ticho.rainbow.interfaces.query.ClientQuery;

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

    /**
     * 根据客户端秘钥查询客户端信息列表
     *
     * @param accessKey 客户端秘钥
     * @return {@link Client}
     */
    Client getByAccessKey(String accessKey);

    /**
     * 查询客户端信息列表
     *
     * @param accessKeys 客户端秘钥列表
     * @return {@link List }<{@link Client }>
     */
    List<Client> getByAccessKeys(List<String> accessKeys);


    /**
     * 根据客户端秘钥删除
     *
     * @param accessKey 客户端秘钥
     */
    void removeByAccessKey(String accessKey);
}

