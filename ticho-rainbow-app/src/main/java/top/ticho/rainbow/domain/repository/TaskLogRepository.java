package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.application.dto.query.TaskLogQuery;
import top.ticho.rainbow.domain.entity.TaskLog;

import java.util.List;

/**
 * 计划任务日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
public interface TaskLogRepository {

    boolean save(TaskLog taskLog);

    /**
     * 根据条件查询计划任务日志信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link TaskLog}>
     */
    List<TaskLog> list(TaskLogQuery query);

    TaskLog find(Long id);

    void removeBefeoreDays(Integer taskParam);

}

