package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.excel.TaskLogExcelExport;
import top.ticho.rainbow.domain.entity.TaskLog;
import top.ticho.rainbow.interfaces.dto.response.TaskLogDTO;
import top.ticho.starter.web.util.TiIdUtil;

/**
 * 计划任务日志信息 转换
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class})
public interface TaskLogAssembler {

    TaskLogDTO toDTO(TaskLog entity);

    TaskLogExcelExport toExcelExport(TaskLogDTO taskLogDTO);

}