package top.ticho.rainbow.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.util.Assert;
import top.ticho.rainbow.application.service.TaskLogService;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.entity.TaskLog;
import top.ticho.rainbow.interfaces.assembler.TaskLogAssembler;
import top.ticho.rainbow.interfaces.dto.TaskLogDTO;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 计划任务日志信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Service
public class TaskLogServiceImpl implements TaskLogService {

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Override
    public TaskLogDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        TaskLog taskLog = taskLogRepository.getById(id);
        return TaskLogAssembler.INSTANCE.entityToDto(taskLog);
    }

    @Override
    public PageResult<TaskLogDTO> page(TaskLogQuery query) {
        // @formatter:off
        query.checkPage();
        Page<TaskLog> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        taskLogRepository.list(query);
        List<TaskLogDTO> taskLogDTOs = page.getResult()
            .stream()
            .map(TaskLogAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), taskLogDTOs);
        // @formatter:on
    }
}
