package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.query.OpLogQuery;
import top.ticho.starter.datasource.service.TiRepository;

import java.util.List;

/**
 * 日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
public interface OpLogRepository extends TiRepository<OpLog> {

    /**
     * 根据条件查询日志信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link OpLog}>
     */
    List<OpLog> list(OpLogQuery query);

}

