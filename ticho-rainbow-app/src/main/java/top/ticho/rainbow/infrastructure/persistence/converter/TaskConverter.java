package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.infrastructure.persistence.po.TaskPO;
import top.ticho.rainbow.interfaces.dto.response.TaskDTO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:21
 */
@Mapper(componentModel = "spring")
public interface TaskConverter {

    Task toEntity(TaskPO po);

    List<Task> toEntity(List<TaskPO> pos);

    TaskPO toPo(Task task);

    List<TaskPO> toPo(List<Task> tasks);

    TaskDTO toDTO(TaskPO taskPO);

}
