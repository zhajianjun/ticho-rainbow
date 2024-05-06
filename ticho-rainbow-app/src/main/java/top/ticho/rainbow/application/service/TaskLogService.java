package top.ticho.rainbow.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.rainbow.interfaces.dto.TaskLogDTO;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;


/**
 * 计划任务日志信息 服务接口
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
public interface TaskLogService {

    /**
     * 根据id查询计划任务日志信息
     *
     * @param id 主键
     * @return {@link TaskLogDTO}
     */
    TaskLogDTO getById(Long id);

    /**
     * 分页查询计划任务日志信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link TaskLogDTO}>
     */
    PageResult<TaskLogDTO> page(TaskLogQuery query);

}

