package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.rainbow.interfaces.query.PortQuery;
import top.ticho.starter.datasource.service.TiRepository;

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
public interface PortRepository extends TiRepository<Port> {

    /**
     * 根据条件查询端口信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Port}>
     */
    List<Port> list(PortQuery query);

    /**
     * 根据端口号查询，排除id
     *
     * @param excludeId 排除的id
     * @param port      端口号
     * @return {@link Port}
     */
    Port getByPortExcludeId(Long excludeId, Integer port);

    /**
     * 根据域名查询，排除id
     *
     * @param excludeId 排除的id
     * @param domain    域名
     * @return {@link Port}
     */
    Port getByDomainExcludeId(Long excludeId, String domain);

    /**
     * 根据客户端秘钥列表查询
     *
     * @param accessKeys 客户端秘钥列表
     * @return {@link List}<{@link Port}>
     */
    List<Port> listByAccessKeys(Collection<String> accessKeys);

    /**
     * 根据客户端秘钥列表查询
     *
     * @param accessKeys 客户端秘钥列表
     * @return {@link List}<{@link Port}>
     */
    <T> Map<String, List<T>> listAndGroupByAccessKey(Collection<String> accessKeys, Function<Port, T> function, Predicate<Port> filter);

}

