package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.command.TaskModifyCommand;
import top.ticho.rainbow.application.dto.command.TaskSaveCommand;
import top.ticho.rainbow.application.dto.excel.TaskExcelExport;
import top.ticho.rainbow.application.dto.response.TaskDTO;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.domain.entity.vo.TaskModifyVo;
import top.ticho.starter.web.util.TiIdUtil;

/**
 * 计划任务信息 转换
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class})
public interface TaskAssembler {

    Task toEntity(TaskSaveCommand taskSaveCommand);

    TaskDTO toDTO(Task entity);

    TaskExcelExport toExcelExport(TaskDTO taskDTO);

    TaskModifyVo toVo(TaskModifyCommand taskModifyCommand);

}