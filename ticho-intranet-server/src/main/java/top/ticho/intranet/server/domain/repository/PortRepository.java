package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Port;
import top.ticho.intranet.server.interfaces.query.PortQuery;

import java.util.List;

/**
 * 端口信息 repository接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface PortRepository extends RootService<Port> {

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

}

