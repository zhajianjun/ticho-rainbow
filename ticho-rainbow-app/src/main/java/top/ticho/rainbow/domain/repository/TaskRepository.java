package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Task;

import java.util.List;

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

    boolean modifyBatch(List<Task> tasks);

    Task find(Long id);

    List<Task> list(List<Long> ids);

    List<Task> all();

}

