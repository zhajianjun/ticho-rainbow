package top.ticho.rainbow.infrastructure.mapper;

import top.ticho.boot.datasource.mapper.RootMapper;
import top.ticho.rainbow.infrastructure.entity.Task;
import org.springframework.stereotype.Repository;


/**
 * 计划任务信息 mapper
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Repository
public interface TaskMapper extends RootMapper<Task> {

}