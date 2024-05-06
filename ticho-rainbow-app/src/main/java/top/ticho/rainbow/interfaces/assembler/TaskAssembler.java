package top.ticho.rainbow.interfaces.assembler;

import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 计划任务信息 转换
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Mapper
public interface TaskAssembler {
    TaskAssembler INSTANCE = Mappers.getMapper(TaskAssembler.class);

    /**
     * 计划任务信息
     *
     * @param dto 计划任务信息DTO
     * @return {@link Task}
     */
    Task dtoToEntity(TaskDTO dto);

    /**
     * 计划任务信息DTO
     *
     * @param entity 计划任务信息
     * @return {@link TaskDTO}
     */
    TaskDTO entityToDto(Task entity);

}