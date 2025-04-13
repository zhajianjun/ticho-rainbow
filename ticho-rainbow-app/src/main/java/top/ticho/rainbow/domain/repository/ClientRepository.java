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

    /**
     * 保存客户端
     *
     * @param client 客户
     * @return boolean
     */
    boolean save(Client client);

    /**
     * 删除客户端
     *
     * @param id 编号
     * @return boolean
     */
    boolean remove(Long id);

    /**
     * 修改客户端
     *
     * @param client 客户端信息
     * @return boolean
     */
    boolean modify(Client client);

    /**
     * 根据编号查询客户端信息
     *
     * @param id 编号
     * @return {@link Client }
     */
    Client find(Long id);

    /**
     * 根据客户端秘钥删除
     *
     * @param accessKey 客户端秘钥
     */
    void removeByAccessKey(String accessKey);

    /**
     * 根据客户端秘钥查询客户端信息列表
     *
     * @param accessKey 客户端秘钥
     * @return {@link Client }
     */
    Client findByAccessKey(String accessKey);

    /**
     * 查询客户端信息列表
     *
     * @param accessKeys 客户端秘钥列表
     * @return {@link List }<{@link Client }>
     */
    List<Client> listByAccessKeys(List<String> accessKeys);

    /**
     * 查询有效客户端信息列表
     *
     * @return {@link List }<{@link Client }>
     */
    List<Client> listEffect();

}

