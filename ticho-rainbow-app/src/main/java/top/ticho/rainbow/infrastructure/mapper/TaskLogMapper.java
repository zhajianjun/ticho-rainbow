package top.ticho.rainbow.infrastructure.mapper;

import top.ticho.boot.datasource.mapper.RootMapper;
import top.ticho.rainbow.infrastructure.entity.TaskLog;
import org.springframework.stereotype.Repository;


/**
 * 计划任务日志信息 mapper
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Repository
public interface TaskLogMapper extends RootMapper<TaskLog> {

}