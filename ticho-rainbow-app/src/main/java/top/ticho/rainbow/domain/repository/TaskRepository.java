package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Task;

import java.util.Collection;

/**
 * 计划任务信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
public interface TaskRepository {

    boolean save(Task task);

    boolean remove(Long id);

    boolean modify(Task task);

    Task find(Long id);

    /**
     * 更新状态 (批量)
     *
     * @param ids    编号列表
     * @param status 状态
     * @return boolean
     */
    boolean modifyStatusBatch(Collection<Long> ids, Integer status);


}

