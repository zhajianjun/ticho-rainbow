package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.TaskAssembler;
import top.ticho.rainbow.application.dto.command.TaskModifyCommand;
import top.ticho.rainbow.application.dto.command.TaskSaveCommand;
import top.ticho.rainbow.application.dto.excel.TaskExp;
import top.ticho.rainbow.application.dto.query.TaskQuery;
import top.ticho.rainbow.application.dto.response.TaskDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.domain.entity.vo.TaskModifyVo;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.core.component.TaskTemplate;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private final TaskAssembler taskAssembler;
    private final List<AbstracTask<?>> abstracTasks;
    private final TaskTemplate taskTemplate;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    public void afterPropertiesSet() {
        List<Task> tasks = taskRepository.list(new TaskQuery());
        List<String> jobs = taskTemplate.listJobs();
        for (Task task : tasks) {
            String jobId = task.getId().toString();
            if (!jobs.contains(jobId)) {
                taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
            }
            if (Objects.equals(task.getStatus(), 1)) {
                taskTemplate.resumeJob(jobId, DEFAULT_JOB_GROUP);
            } else {
                taskTemplate.pauseJob(jobId, DEFAULT_JOB_GROUP);
            }
            jobs.remove(jobId);
        }
        if (!jobs.isEmpty()) {
            jobs.forEach(jobId -> taskTemplate.deleteJob(jobId, DEFAULT_JOB_GROUP));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(TaskSaveCommand taskSaveCommand) {
        check(taskSaveCommand.getCronExpression(), taskSaveCommand.getContent(), taskSaveCommand.getParam());
        Task task = taskAssembler.toEntity(taskSaveCommand);
        TiAssert.isTrue(taskRepository.save(task), TiBizErrCode.FAIL, "保存失败");
        boolean addedJob = taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        TiAssert.isTrue(addedJob, TiBizErrCode.FAIL, "添加任务失败");
        if (Objects.equals(task.getStatus(), 1)) {
            taskTemplate.resumeJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        } else {
            taskTemplate.pauseJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        }
    }

    private void check(String cronExpression, String content, String param) {
        boolean valid = TaskTemplate.isValid(cronExpression);
        TiAssert.isTrue(valid, TiBizErrCode.FAIL, "cron表达式不正确");
        Optional<AbstracTask<?>> abstracTaskOpt = abstracTasks
            .stream()
            .filter(x -> Objects.equals(x.getClass().getName(), content))
            .findFirst();
        if (!abstracTaskOpt.isPresent()) {
            TiAssert.cast(TiBizErrCode.PARAM_ERROR, "执行类不存在");
        }
        AbstracTask<?> abstracTask = abstracTaskOpt.get();

        if (StrUtil.isNotBlank(param)) {
            Object taskParam = abstracTask.getTaskParam(param);
            TiAssert.isNotNull(taskParam, TiBizErrCode.FAIL, "参数格式不正确");
        }
    }

    public void remove(Long id) {
        TiAssert.isTrue(taskRepository.remove(id), TiBizErrCode.FAIL, "删除失败");
        boolean deleteJob = taskTemplate.deleteJob(id.toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(deleteJob, TiBizErrCode.FAIL, "删除任务失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(TaskModifyCommand taskModifyCommand) {
        Long id = taskModifyCommand.getId();
        check(taskModifyCommand.getCronExpression(), taskModifyCommand.getContent(), taskModifyCommand.getParam());
        Task task = taskRepository.find(id);
        TaskModifyVo taskModifyVo = taskAssembler.toVo(taskModifyCommand);
        task.modify(taskModifyVo);
        TiAssert.isTrue(taskRepository.modify(task), TiBizErrCode.FAIL, "修改失败");
        boolean exists = taskTemplate.checkExists(id.toString(), DEFAULT_JOB_GROUP);
        if (exists) {
            boolean deleteJob = taskTemplate.deleteJob(task.getId().toString(), DEFAULT_JOB_GROUP);
            TiAssert.isTrue(deleteJob, TiBizErrCode.FAIL, "删除任务失败");
        }
        Task dbTask = taskRepository.find(task.getId());
        Integer status = dbTask.getStatus();
        boolean addedJob = taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        TiAssert.isTrue(addedJob, TiBizErrCode.FAIL, "添加任务失败");
        if (Objects.equals(status, 1)) {
            taskTemplate.resumeJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        }
    }

    public void runOnce(Long id, String param) {
        Task task = taskRepository.find(id);
        TiAssert.isNotNull(task, TiBizErrCode.FAIL, "任务不存在");
        boolean exists = taskTemplate.checkExists(task.getId().toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(exists, TiBizErrCode.FAIL, "任务不存在");
        check(task.getCronExpression(), task.getContent(), task.getParam());
        boolean runJob = taskTemplate.runOnce(task.getId().toString(), DEFAULT_JOB_GROUP, param);
        TiAssert.isTrue(runJob, TiBizErrCode.FAIL, "立即执行任务失败");
    }

    public void pause(Long id) {
        Task task = taskRepository.find(id);
        TiAssert.isNotNull(task, TiBizErrCode.FAIL, "任务不存在");
        Integer status = task.getStatus();
        TiAssert.isTrue(Objects.equals(status, 1), TiBizErrCode.FAIL, "任务未启动");
        boolean exists = taskTemplate.checkExists(id.toString(), DEFAULT_JOB_GROUP);
        boolean updated = taskRepository.updateStatusBatch(Collections.singletonList(id), 0);
        TiAssert.isTrue(updated, TiBizErrCode.FAIL, "暂停任务失败");
        TiAssert.isTrue(exists, TiBizErrCode.FAIL, "任务不存在");
        boolean pauseJob = taskTemplate.pauseJob(id.toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(pauseJob, TiBizErrCode.FAIL, "暂停任务失败");
    }

    public void resume(Long id) {
        Task task = taskRepository.find(id);
        TiAssert.isNotNull(task, TiBizErrCode.FAIL, "任务不存在");
        Integer status = task.getStatus();
        TiAssert.isTrue(Objects.equals(status, 0), TiBizErrCode.FAIL, "任务已启动");
        boolean updated = taskRepository.updateStatusBatch(Collections.singletonList(id), 1);
        TiAssert.isTrue(updated, TiBizErrCode.FAIL, "恢复任务失败");
        boolean exists = taskTemplate.checkExists(id.toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(exists, TiBizErrCode.FAIL, "任务不存在");
        boolean resumeJob = taskTemplate.resumeJob(id.toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(resumeJob, TiBizErrCode.FAIL, "恢复任务失败");
    }

    public List<String> getRecentCronTime(String cronExpression, Integer num) {
        num = Optional.ofNullable(num).orElse(10);
        TiAssert.isTrue(num > 0, TiBizErrCode.FAIL, "查询数量必须大于0");
        TiAssert.isTrue(TaskTemplate.isValid(cronExpression), TiBizErrCode.FAIL, "cron表达式不合法");
        return TaskTemplate.getRecentCronTime(cronExpression, num);
    }


    public TaskDTO getById(Long id) {
        Task task = taskRepository.find(id);
        return taskAssembler.toDTO(task);
    }

    public TiPageResult<TaskDTO> page(TaskQuery query) {
        query.checkPage();
        Page<Task> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        taskRepository.list(query);
        List<TaskDTO> taskModifyCommands = page.getResult()
            .stream()
            .map(taskAssembler::toDTO)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), taskModifyCommands);
    }

    public List<TaskDTO> list(TaskQuery query) {
        List<Task> list = taskRepository.list(query);
        return list.stream()
            .map(taskAssembler::toDTO)
            .collect(Collectors.toList());
    }

    public void exportExcel(TaskQuery query) throws IOException {
        String sheetName = "计划任务";
        String fileName = "计划任务导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.PLAN_TASK);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, TaskExp.class, response);
    }

    private Collection<TaskExp> excelExpHandle(TaskQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<Task> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        taskRepository.list(query);
        return page.getResult()
            .stream()
            .map(x -> {
                TaskExp taskExp = taskAssembler.toExp(x);
                taskExp.setStatusName(labelMap.get(DictConst.COMMON_STATUS + x.getStatus()));
                taskExp.setContent(labelMap.get(DictConst.PLAN_TASK + x.getContent()));
                return taskExp;
            })
            .collect(Collectors.toList());
    }

}
