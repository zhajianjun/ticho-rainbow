package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-13 18:05
 */
public interface OpLogAppRepository {

    /**
     * 根据条件查询日志信息列表
     *
     * @param query 查询条件
     */
    TiPageResult<OpLogDTO> page(OpLogQuery query);

}
