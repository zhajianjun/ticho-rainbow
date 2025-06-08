package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.OpLog;

/**
 * 日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
public interface OpLogRepository {

    void save(OpLog entity);

    OpLog find(Long id);

    void removeBefeoreDays(Integer days);


}

