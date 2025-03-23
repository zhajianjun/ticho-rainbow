package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.response.TaskLogDTO;
import top.ticho.rainbow.application.dto.excel.TaskLogExp;
import top.ticho.rainbow.domain.entity.TaskLog;

/**
 * 计划任务日志信息 转换
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Mapper(componentModel = "spring")
public interface TaskLogAssembler {

    TaskLogDTO toDTO(TaskLog entity);

    TaskLogExp toExp(TaskLog entity);

}