package top.ticho.rainbow.domain.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.view.core.TiPageResult;
import top.ticho.boot.view.enums.TiBizErrCode;
import top.ticho.boot.view.util.TiAssert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.task.TaskService;
import top.ticho.rainbow.domain.handle.DictHandle;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.core.component.TaskTemplate;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.rainbow.interfaces.assembler.TaskAssembler;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.rainbow.interfaces.excel.TaskExp;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
@Service
public class TaskServiceImpl implements TaskService {
    public static final String DEFAULT_JOB_GROUP = "DEFAULT_JOB_GROUP";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private List<AbstracTask<?>> abstracTasks;

    @Autowired
    private TaskTemplate taskTemplate;

    @Autowired
    private DictHandle dictHandle;

    @Resource
    private HttpServletResponse response;


    @PostConstruct
    public void init() {
        List<Task> tasks = taskRepository.list();
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TaskDTO taskDTO) {
        ValidUtil.valid(taskDTO);
        Task task = TaskAssembler.INSTANCE.dtoToEntity(taskDTO);
        task.setId(CloudIdUtil.getId());
        TiAssert.isTrue(taskRepository.save(task), TiBizErrCode.FAIL, "保存失败");
        check(task);
        boolean addedJob = taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        TiAssert.isTrue(addedJob, TiBizErrCode.FAIL, "添加任务失败");
        if (Objects.equals(task.getStatus(), 1)) {
            taskTemplate.resumeJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        } else {
            taskTemplate.pauseJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        }
    }

    private void check(Task task) {
        boolean valid = TaskTemplate.isValid(task.getCronExpression());
        TiAssert.isTrue(valid, TiBizErrCode.FAIL, "cron表达式不正确");
        Optional<AbstracTask<?>> abstracTaskOpt = abstracTasks
            .stream()
            .filter(x -> Objects.equals(x.getClass().getName(), task.getContent()))
            .findFirst();
        if (!abstracTaskOpt.isPresent()) {
            TiAssert.cast(TiBizErrCode.PARAM_ERROR, "执行类不存在");
        }
        AbstracTask<?> abstracTask = abstracTaskOpt.get();
        String param = task.getParam();
        if (StrUtil.isNotBlank(param)) {
            Object taskParam = abstracTask.getTaskParam(param);
            TiAssert.isNotNull(taskParam, TiBizErrCode.FAIL, "参数格式不正确");
        }
    }

    @Override
    public void remove(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        TiAssert.isTrue(taskRepository.removeById(id), TiBizErrCode.FAIL, "删除失败");
        boolean deleteJob = taskTemplate.deleteJob(id.toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(deleteJob, TiBizErrCode.FAIL, "删除任务失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TaskDTO taskDTO) {
        ValidUtil.valid(taskDTO, ValidGroup.Upd.class);
        Task task = TaskAssembler.INSTANCE.dtoToEntity(taskDTO);
        check(task);
        TiAssert.isTrue(taskRepository.updateById(task), TiBizErrCode.FAIL, "修改失败");
        boolean exists = taskTemplate.checkExists(task.getId().toString(), DEFAULT_JOB_GROUP);
        if (exists) {
            boolean deleteJob = taskTemplate.deleteJob(task.getId().toString(), DEFAULT_JOB_GROUP);
            TiAssert.isTrue(deleteJob, TiBizErrCode.FAIL, "删除任务失败");
        }
        Task dbTask = taskRepository.getById(task.getId());
        Integer status = dbTask.getStatus();
        boolean addedJob = taskTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        TiAssert.isTrue(addedJob, TiBizErrCode.FAIL, "添加任务失败");
        if (Objects.equals(status, 1)) {
            taskTemplate.resumeJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        }
    }

    @Override
    public void runOnce(Long id, String param) {
        TiAssert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
        TiAssert.isNotNull(task, TiBizErrCode.FAIL, "任务不存在");
        boolean exists = taskTemplate.checkExists(task.getId().toString(), DEFAULT_JOB_GROUP);
        TiAssert.isTrue(exists, TiBizErrCode.FAIL, "任务不存在");
        check(task);
        boolean runJob = taskTemplate.runOnce(task.getId().toString(), DEFAULT_JOB_GROUP, param);
        TiAssert.isTrue(runJob, TiBizErrCode.FAIL, "立即执行任务失败");
    }

    @Override
    public void pause(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
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

    @Override
    public void resume(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
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

    @Override
    public List<String> getRecentCronTime(String cronExpression, Integer num) {
        num = Optional.ofNullable(num).orElse(10);
        TiAssert.isTrue(num > 0, TiBizErrCode.FAIL, "查询数量必须大于0");
        TiAssert.isTrue(TaskTemplate.isValid(cronExpression), TiBizErrCode.FAIL, "cron表达式不合法");
        return TaskTemplate.getRecentCronTime(cronExpression, num);
    }

    @Override
    public TaskDTO getById(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
        return TaskAssembler.INSTANCE.entityToDto(task);
    }

    @Override
    public TiPageResult<TaskDTO> page(TaskQuery query) {
        query.checkPage();
        Page<Task> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        taskRepository.list(query);
        List<TaskDTO> taskDTOs = page.getResult()
            .stream()
            .map(TaskAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), taskDTOs);
    }

    @Override
    public List<TaskDTO> list(TaskQuery query) {
        List<Task> list = taskRepository.list(query);
        return list.stream()
            .map(TaskAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public void expExcel(TaskQuery query) throws IOException {
        String sheetName = "计划任务";
        String fileName = "计划任务导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictHandle.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.PLAN_TASK);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, TaskExp.class, response);
    }

    private Collection<TaskExp> excelExpHandle(TaskQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<Task> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        taskRepository.list(query);
        return page.getResult()
            .stream()
            .map(x -> {
                TaskExp taskExp = TaskAssembler.INSTANCE.entityToExp(x);
                taskExp.setStatusName(labelMap.get(DictConst.COMMON_STATUS + x.getStatus()));
                taskExp.setContent(labelMap.get(DictConst.PLAN_TASK + x.getContent()));
                return taskExp;
            })
            .collect(Collectors.toList());
    }

}
