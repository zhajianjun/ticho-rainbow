package top.ticho.rainbow.application.task;

import top.ticho.boot.view.core.TiPageResult;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import java.io.IOException;
import java.util.List;


/**
 * 计划任务 服务接口
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
public interface TaskService {
    /**
     * 保存计划任务
     *
     * @param taskDTO 计划任务DTO 对象
     */
    void save(TaskDTO taskDTO);

    /**
     * 删除计划任务
     *
     * @param id 主键
     */
    void remove(Long id);

    /**
     * 修改计划任务
     *
     * @param taskDTO 计划任务DTO 对象
     */
    void update(TaskDTO taskDTO);

    /**
     * 执行一次计划任务
     *
     * @param id    主键
     * @param param 任务参数
     */
    void runOnce(Long id, String param);

    /**
     * 暂停计划任务
     *
     * @param id 主键
     */
    void pause(Long id);

    /**
     * 恢复计划任务
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
     * 根据id查询计划任务
     *
     * @param id 主键
     * @return {@link TaskDTO}
     */
    TaskDTO getById(Long id);

    /**
     * 分页查询计划任务列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link TaskDTO}>
     */
    TiPageResult<TaskDTO> page(TaskQuery query);

    /**
     * 分页查询计划任务列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link TaskDTO}>
     */
    List<TaskDTO> list(TaskQuery query);

    /**
     * 导出计划任务
     *
     * @param query 查询条件
     */
    void expExcel(TaskQuery query) throws IOException;

}

