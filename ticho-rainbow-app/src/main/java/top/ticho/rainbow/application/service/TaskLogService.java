package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.TaskLogAssembler;
import top.ticho.rainbow.application.dto.TaskLogDTO;
import top.ticho.rainbow.application.dto.excel.TaskLogExp;
import top.ticho.rainbow.application.dto.query.TaskLogQuery;
import top.ticho.rainbow.application.dto.query.TaskQuery;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.domain.entity.TaskLog;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;

import javax.servlet.http.HttpServletResponse;
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

    private final TaskLogRepository taskLogRepository;    private final TaskLogAssembler taskLogAssembler;    private final DictExecutor dictExecutor;    private final HttpServletResponse response;    private final TaskRepository taskRepository;
    public TaskLogDTO getById(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        TaskLog taskLog = taskLogRepository.find(id);
        return taskLogAssembler.toDTO(taskLog);
    }

    public TiPageResult<TaskLogDTO> page(TaskLogQuery query) {
        query.checkPage();
        Page<TaskLog> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        taskLogRepository.list(query);
        List<TaskLogDTO> taskLogDTOs = page.getResult()
            .stream()
            .map(taskLogAssembler::toDTO)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), taskLogDTOs);
    }

    public void expExcel(TaskLogQuery query) throws IOException {
        String sheetName = "计划任务日志";
        String fileName = "计划任务日志导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.TASK_LOG_STATUS, DictConst.PLAN_TASK, DictConst.YES_OR_NO);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, TaskLogExp.class, response);
    }

    private Collection<TaskLogExp> excelExpHandle(TaskLogQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<TaskLog> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        taskLogRepository.list(query);
        List<TaskLog> result = page.getResult();
        List<Long> taskIds = result.stream().map(TaskLog::getTaskId).collect(Collectors.toList());
        Map<Long, String> taskMap = getTaskNameMap(taskIds);
        return result
            .stream()
            .map(x -> {
                TaskLogExp taskLogExp = taskLogAssembler.toExp(x);
                taskLogExp.setName(taskMap.get(x.getTaskId()));
                taskLogExp.setStatusName(labelMap.get(DictConst.TASK_LOG_STATUS + x.getStatus()));
                taskLogExp.setContent(labelMap.get(DictConst.PLAN_TASK + x.getContent()));
                taskLogExp.setIsErrName(labelMap.get(DictConst.YES_OR_NO + x.getIsErr()));
                return taskLogExp;
            })
            .collect(Collectors.toList());
    }

    private Map<Long, String> getTaskNameMap(List<Long> taskIds) {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setIds(taskIds);
        List<Task> tasks = taskRepository.list(taskQuery);
        return tasks.stream().collect(Collectors.toMap(Task::getId, Task::getName));
    }

}
