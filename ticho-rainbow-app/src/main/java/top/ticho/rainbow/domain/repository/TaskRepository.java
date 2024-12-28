package top.ticho.rainbow.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import java.util.Collection;
import java.util.List;

/**
 * 计划任务信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
public interface TaskRepository extends RootService<Task> {

    /**
     * 根据条件查询计划任务信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Task}>
     */
    List<Task> list(TaskQuery query);

    /**
     * 更新状态 (批量)
     *
     * @param ids    编号列表
     * @param status 状态
     * @return boolean
     */
    boolean updateStatusBatch(Collection<Long> ids, Integer status);

}

