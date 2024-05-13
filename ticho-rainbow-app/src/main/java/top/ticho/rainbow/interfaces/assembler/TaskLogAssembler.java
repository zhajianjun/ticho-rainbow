package top.ticho.rainbow.interfaces.assembler;

import top.ticho.rainbow.infrastructure.entity.TaskLog;
import top.ticho.rainbow.interfaces.dto.TaskLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.rainbow.interfaces.excel.TaskLogExp;

/**
 * 计划任务日志信息 转换
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Mapper
public interface TaskLogAssembler {
    TaskLogAssembler INSTANCE = Mappers.getMapper(TaskLogAssembler.class);

    /**
     * 计划任务日志信息
     *
     * @param dto 计划任务日志信息DTO
     * @return {@link TaskLog}
     */
    TaskLog dtoToEntity(TaskLogDTO dto);

    /**
     * 计划任务日志信息DTO
     *
     * @param entity 计划任务日志信息
     * @return {@link TaskLogDTO}
     */
    TaskLogDTO entityToDto(TaskLog entity);

    TaskLogExp entityToExp(TaskLog entity);
}