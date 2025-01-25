package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.infrastructure.entity.TaskLog;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;
import top.ticho.starter.datasource.service.TiRepository;

import java.util.List;

/**
 * 计划任务日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
public interface TaskLogRepository extends TiRepository<TaskLog> {

    /**
     * 根据条件查询计划任务日志信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link TaskLog}>
     */
    List<TaskLog> list(TaskLogQuery query);

}

