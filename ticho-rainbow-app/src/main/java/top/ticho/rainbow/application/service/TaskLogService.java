package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.TaskLogAssembler;
import top.ticho.rainbow.application.dto.excel.TaskLogExcelExport;
import top.ticho.rainbow.application.dto.query.TaskLogQuery;
import top.ticho.rainbow.application.dto.response.TaskDTO;
import top.ticho.rainbow.application.dto.response.TaskLogDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.TaskAppRepository;
import top.ticho.rainbow.application.repository.TaskLogAppRepository;
import top.ticho.rainbow.domain.entity.TaskLog;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.starter.view.core.TiPageResult;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计划任务日志信息
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@RequiredArgsConstructor
@Service
public class TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskLogAppRepository taskLogAppRepository;
    private final TaskLogAssembler taskLogAssembler;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;
    private final TaskAppRepository taskAppRepository;

    public TaskLogDTO find(Long id) {
        TaskLog taskLog = taskLogRepository.find(id);
        return taskLogAssembler.toDTO(taskLog);
    }

    public TiPageResult<TaskLogDTO> page(TaskLogQuery query) {
        return taskLogAppRepository.page(query);
    }

    public void exportExcel(TaskLogQuery query) throws IOException {
        String sheetName = "计划任务日志";
        String fileName = "计划任务日志导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.TASK_LOG_STATUS, DictConst.PLAN_TASK, DictConst.YES_OR_NO);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, TaskLogExcelExport.class, response);
    }

    private Collection<TaskLogExcelExport> excelExpHandle(TaskLogQuery query, Map<String, String> labelMap) {
        TiPageResult<TaskLogDTO> page = taskLogAppRepository.page(query);
        List<TaskLogDTO> result = page.getRows();
        List<Long> taskIds = result.stream().map(TaskLogDTO::getTaskId).collect(Collectors.toList());
        Map<Long, String> taskMap = getTaskNameMap(taskIds);
        return result
            .stream()
            .map(x -> {
                TaskLogExcelExport taskLogExcelExport = taskLogAssembler.toExcelExport(x);
                taskLogExcelExport.setName(taskMap.get(x.getTaskId()));
                taskLogExcelExport.setStatusName(labelMap.get(DictConst.TASK_LOG_STATUS + x.getStatus()));
                taskLogExcelExport.setContent(labelMap.get(DictConst.PLAN_TASK + x.getContent()));
                taskLogExcelExport.setIsErrName(labelMap.get(DictConst.YES_OR_NO + x.getIsErr()));
                return taskLogExcelExport;
            })
            .collect(Collectors.toList());
    }

    private Map<Long, String> getTaskNameMap(List<Long> taskIds) {
        List<TaskDTO> tasks = taskAppRepository.all();
        return tasks
            .stream()
            .filter(x -> taskIds.contains(x.getId()))
            .collect(Collectors.toMap(TaskDTO::getId, TaskDTO::getName));
    }

}
