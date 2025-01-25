package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 计划任务信息 mapper
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Repository
public interface TaskMapper extends TiMapper<Task> {

}