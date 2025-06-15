package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.TaskAssembler;
import top.ticho.rainbow.application.dto.excel.TaskExcelExport;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.TaskAppRepository;
import top.ticho.rainbow.application.task.AbstracTask;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.domain.entity.vo.TaskModifyVO;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.common.component.TaskTemplate;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.interfaces.dto.command.TaskModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.TaskRunOnceCommand;
import top.ticho.rainbow.interfaces.dto.command.TaskSaveCommand;
import top.ticho.rainbow.interfaces.dto.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.query.TaskQuery;
import top.ticho.rainbow.interfaces.dto.response.TaskDTO;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrorCode;
import top.ticho.starter.view.util.TiAssert;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 计划任务 服务实现
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@RequiredArgsConstructor
@Service
public class TaskService implements InitializingBean {
    public static final String DEFAULT_JOB_GROUP = "DEFAULT_JOB_GROUP";

    private final TaskRepository taskRepository;
    private final TaskAppRepository taskAppRepository;
    private final TaskAssembler taskAssembler;
    private final List<AbstracTask<?>> abstracTasks;
    private final TaskTemplate taskTemplate;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    public void afterPropertiesSet() {
        taskTemplate.start();
        List<Task> tasks = taskRepository.all();
        for (Task task : tasks) {
            // 添加任务
            taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
            // 暂停任务
            if (!task.isEnable()) {
                taskTemplate.pauseJob(task.getId().toString(), DEFAULT_JOB_GROUP);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(TaskSaveCommand taskSaveCommand) {
        check(taskSaveCommand.getCronExpression(), taskSaveCommand.getContent(), taskSaveCommand.getParam());
        Task task = taskAssembler.toEntity(taskSaveCommand);
        TiAssert.isTrue(taskRepository.save(task), "保存失败");
        taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        taskTemplate.pauseJob(task.getId().toString(), DEFAULT_JOB_GROUP);
    }

    private void check(String cronExpression, String content, String param) {
        boolean valid = TaskTemplate.isValid(cronExpression);
        TiAssert.isTrue(valid, "cron表达式不正确");
        Optional<AbstracTask<?>> abstracTaskOpt = abstracTasks
            .stream()
            .filter(x -> Objects.equals(x.getClass().getName(), content))
            .findFirst();
        if (abstracTaskOpt.isEmpty()) {
            TiAssert.cast(TiBizErrorCode.PARAM_ERROR, "执行类不存在");
        }
        AbstracTask<?> abstracTask = abstracTaskOpt.get();

        if (StrUtil.isNotBlank(param)) {
            Object taskParam = abstracTask.getTaskParam(param);
            TiAssert.isNotNull(taskParam, "参数格式不正确");
        }
    }

    public void remove(VersionModifyCommand command) {
        Task task = taskRepository.find(command.getId());
        TiAssert.isNotNull(task, "删除失败，任务不存在");
        task.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(!task.isEnable(), "删除失败，请先禁用该任务");
        TiAssert.isTrue(taskRepository.remove(command.getId()), "删除失败，请刷新后重试");
        boolean deleteJob = taskTemplate.deleteJob(command.getId().toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(deleteJob, "删除任务失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(TaskModifyCommand taskModifyCommand) {
        Long id = taskModifyCommand.getId();
        check(taskModifyCommand.getCronExpression(), taskModifyCommand.getContent(), taskModifyCommand.getParam());
        Task task = taskRepository.find(id);
        TiAssert.isNotNull(task, "修改失败，任务不存在");
        task.checkVersion(taskModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        TaskModifyVO taskModifyVo = taskAssembler.toVo(taskModifyCommand);
        task.modify(taskModifyVo);
        TiAssert.isTrue(taskRepository.modify(task), "修改失败，请刷新后重试");
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, this::enable);
        TiAssert.isTrue(enable, "启用任务失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, this::disable);
        TiAssert.isTrue(disable, "禁用任务失败，请刷新后重试");
    }

    public void runOnce(TaskRunOnceCommand taskRunOnceCommand) {
        Task task = taskRepository.find(taskRunOnceCommand.getId());
        TiAssert.isNotNull(task, "任务不存在");
        boolean exists = taskTemplate.checkExists(task.getId().toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(exists, "任务不存在");
        check(task.getCronExpression(), task.getContent(), task.getParam());
        boolean runJob = taskTemplate.runOnce(task.getId().toString(), DEFAULT_JOB_GROUP, taskRunOnceCommand.getParam());
        TiAssert.isTrue(runJob, "立即执行任务失败");
    }

    public List<String> recentCronTime(String cronExpression, Integer num) {
        num = Optional.ofNullable(num).orElse(10);
        TiAssert.isTrue(num > 0, "查询数量必须大于0");
        TiAssert.isTrue(TaskTemplate.isValid(cronExpression), "cron表达式不合法");
        return TaskTemplate.getRecentCronTime(cronExpression, num);
    }

    public TiPageResult<TaskDTO> page(TaskQuery query) {
        return taskAppRepository.page(query);
    }

    public List<TaskDTO> all() {
        List<Task> all = taskRepository.all();
        return taskAssembler.toDTO(all);
    }

    public void exportExcel(TaskQuery query) throws IOException {
        String sheetName = "计划任务";
        String fileName = "计划任务导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.PLAN_TASK);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, TaskExcelExport.class, response);
    }

    private Collection<TaskExcelExport> excelExpHandle(TaskQuery query, Map<String, String> labelMap) {
        TiPageResult<TaskDTO> page = taskAppRepository.page(query);
        return page.getRows()
            .stream()
            .map(x -> {
                TaskExcelExport taskExcelExport = taskAssembler.toExcelExport(x);
                taskExcelExport.setStatusName(labelMap.get(DictConst.COMMON_STATUS + x.getStatus()));
                taskExcelExport.setContent(labelMap.get(DictConst.PLAN_TASK + x.getContent()));
                return taskExcelExport;
            })
            .collect(Collectors.toList());
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<Task> modifyHandle) {
        List<Long> ids = CollStreamUtil.toList(modifys, VersionModifyCommand::getId);
        List<Task> tasks = taskRepository.list(ids);
        Map<Long, Task> taskMap = CollStreamUtil.toIdentityMap(tasks, Task::getId);
        for (VersionModifyCommand modify : modifys) {
            Task task = taskMap.get(modify.getId());
            TiAssert.isNotNull(task, StrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            task.checkVersion(modify.getVersion(), StrUtil.format("数据已被修改，请刷新后重试, 任务: {}", task.getName()));
            // 修改逻辑
            modifyHandle.accept(task);
        }
        return taskRepository.modifyBatch(tasks);
    }

    public void enable(Task task) {
        task.enable();
        String taskId = task.getId().toString();
        taskTemplate.resumeJob(taskId, DEFAULT_JOB_GROUP);
    }

    public void disable(Task task) {
        task.disable();
        String taskId = task.getId().toString();
        taskTemplate.pauseJob(taskId, DEFAULT_JOB_GROUP);
    }

}
