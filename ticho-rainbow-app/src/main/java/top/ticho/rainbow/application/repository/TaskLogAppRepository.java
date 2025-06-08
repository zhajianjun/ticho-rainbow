package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.query.TaskLogQuery;
import top.ticho.rainbow.interfaces.dto.response.TaskLogDTO;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:30
 */
public interface TaskLogAppRepository {

    TiPageResult<TaskLogDTO> page(TaskLogQuery query);

}
