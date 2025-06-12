package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.interfaces.dto.query.OpLogQuery;
import top.ticho.rainbow.interfaces.dto.response.OpLogDTO;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-04-13 18:05
 */
public interface OpLogAppRepository {

    /**
     * 根据条件查询日志信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link OpLog}>
     */
    TiPageResult<OpLogDTO> page(OpLogQuery query);

}
