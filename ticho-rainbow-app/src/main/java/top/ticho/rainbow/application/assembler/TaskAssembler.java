package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.excel.TaskExcelExport;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.domain.entity.vo.TaskModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.interfaces.dto.command.TaskModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.TaskSaveCommand;
import top.ticho.rainbow.interfaces.dto.response.TaskDTO;
import top.ticho.starter.web.util.TiIdUtil;

import java.util.List;

/**
 * 计划任务信息 转换
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class, CommonStatus.class})
public interface TaskAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "status", expression = "java(CommonStatus.DISABLE.code())")
    Task toEntity(TaskSaveCommand taskSaveCommand);

    TaskDTO toDTO(Task task);

    List<TaskDTO> toDTO(List<Task> tasks);

    TaskExcelExport toExcelExport(TaskDTO taskDTO);

    TaskModifyVO toVo(TaskModifyCommand taskModifyCommand);

}