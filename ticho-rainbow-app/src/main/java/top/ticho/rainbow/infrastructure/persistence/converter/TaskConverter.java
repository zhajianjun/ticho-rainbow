package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.infrastructure.persistence.po.TaskPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:21
 */
@Mapper(componentModel = "spring")
public interface TaskConverter {

    Task toEntity(TaskPO taskPO);

    List<Task> toEntitys(List<TaskPO> taskPOS);

    TaskPO toPo(Task task);

}
