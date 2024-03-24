package top.ticho.rainbow.interfaces.assembler;

import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 定时任务调度信息 转换
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Mapper
public interface TaskAssembler {
    TaskAssembler INSTANCE = Mappers.getMapper(TaskAssembler.class);

    /**
     * 定时任务调度信息
     *
     * @param dto 定时任务调度信息DTO
     * @return {@link Task}
     */
    Task dtoToEntity(TaskDTO dto);

    /**
     * 定时任务调度信息DTO
     *
     * @param entity 定时任务调度信息
     * @return {@link TaskDTO}
     */
    TaskDTO entityToDto(Task entity);

}