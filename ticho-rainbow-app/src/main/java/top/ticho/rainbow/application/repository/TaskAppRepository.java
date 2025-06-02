package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.application.dto.query.TaskQuery;
import top.ticho.rainbow.application.dto.response.TaskDTO;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:29
 */
public interface TaskAppRepository {

    TiPageResult<TaskDTO> page(TaskQuery query);

}
