package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Client;

import java.util.List;

/**
 * 客户端信息 repository接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface ClientRepository {

    boolean save(Client client);

    boolean remove(Long id);

    boolean modify(Client client);

    boolean modifyBatch(List<Client> clients);

    Client find(Long id);

    List<Client> list(List<Long> ids);

    Client findByAccessKey(String accessKey);

    List<Client> listByAccessKeys(List<String> accessKeys);

    /**
     * 查询有效客户端信息列表
     */
    List<Client> listEffect();

}

