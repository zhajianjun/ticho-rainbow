package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.TaskLog;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 计划任务日志信息 mapper
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Repository
public interface TaskLogMapper extends TiMapper<TaskLog> {

}