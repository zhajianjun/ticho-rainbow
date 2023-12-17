package top.ticho.intranet.server.domain.repository;

import com.ticho.boot.datasource.service.RootService;
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

}

