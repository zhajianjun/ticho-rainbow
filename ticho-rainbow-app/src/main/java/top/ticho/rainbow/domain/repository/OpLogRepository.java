package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.application.dto.query.OpLogQuery;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;

/**
 * 日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
public interface OpLogRepository {


    void save(OpLog entity);

    /**
     * 根据编号查询日志信息
     *
     * @param id 编号
     * @return {@link OpLog }
     */
    OpLog find(Long id);

    void removeBefeoreDays(Integer days);


}

