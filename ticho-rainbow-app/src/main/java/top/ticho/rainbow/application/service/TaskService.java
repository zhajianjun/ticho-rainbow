package top.ticho.rainbow.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import java.util.List;


/**
 * 定时任务调度 服务接口
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
public interface TaskService {
    /**
     * 保存定时任务调度
     *
     * @param taskDTO 定时任务调度DTO 对象
     */
    void save(TaskDTO taskDTO);

    /**
     * 删除定时任务调度
     *
     * @param id 主键
     */
    void remove(Long id);

    /**
     * 修改定时任务调度
     *
     * @param taskDTO 定时任务调度DTO 对象
     */
    void update(TaskDTO taskDTO);

    /**
     * 执行一次定时任务调度
     *
     * @param id    主键
     * @param param 任务参数
     */
    void runOnce(Long id, String param);

    /**
     * 暂停定时任务调度
     *
     * @param id 主键
     */
    void pause(Long id);

    /**
     * 恢复定时任务调度
     *
     * @param id 主键
     */
    void resume(Long id);

    /**
     * 根据cron表达式获取近n次的执行时间
     *
     * @param cronExpression cron表达式
     * @param num            次数
     * @return {@link List}<{@link String}>
     */
    List<String> getRecentCronTime(String cronExpression, Integer num);


    /**
     * 根据id查询定时任务调度
     *
     * @param id 主键
     * @return {@link TaskDTO}
     */
    TaskDTO getById(Long id);

    /**
     * 分页查询定时任务调度列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link TaskDTO}>
     */
    PageResult<TaskDTO> page(TaskQuery query);

    /**
     * 分页查询定时任务调度列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link TaskDTO}>
     */
    List<TaskDTO> list(TaskQuery query);

}

