package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.OpLog;
import top.ticho.intranet.server.interfaces.query.OpLogQuery;

import java.util.List;

/**
 * 日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface OpLogRepository extends RootService<OpLog> {

    /**
     * 根据条件查询OpLog列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link OpLog}>
     */
    List<OpLog> list(OpLogQuery query);

}

