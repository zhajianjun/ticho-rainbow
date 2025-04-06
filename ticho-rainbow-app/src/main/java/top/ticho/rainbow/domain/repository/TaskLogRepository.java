package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.TaskLog;

/**
 * 计划任务日志信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
public interface TaskLogRepository {

    boolean save(TaskLog taskLog);

    TaskLog find(Long id);

    void removeBefeoreDays(Integer taskParam);

}

