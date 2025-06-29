package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Port;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 端口信息 repository接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface PortRepository {

    boolean save(Port port);

    boolean remove(Long id);

    boolean modify(Port port);

    boolean modifyBatch(List<Port> ports);

    Port find(Long id);

    List<Port> list(List<Long> ids);

    /**
     * 根据端口号查询，排除id
     *
     * @param excludeId 排除的id
     * @param port      端口号
     */
    Port getByPortExcludeId(Long excludeId, Integer port);

    /**
     * 根据域名查询，排除id
     *
     * @param excludeId 排除的id
     * @param domain    域名
     */
    Port getByDomainExcludeId(Long excludeId, String domain);

    /**
     * 根据客户端秘钥列表查询
     *
     * @param accessKeys 客户端秘钥列表
     */
    List<Port> listByAccessKeys(Collection<String> accessKeys);

    /**
     * 根据客户端秘钥列表查询
     *
     * @param accessKeys 客户端秘钥列表
     */
    <T> Map<String, List<T>> listAndGroupByAccessKey(Collection<String> accessKeys, Function<Port, T> function, Predicate<Port> filter);

}

