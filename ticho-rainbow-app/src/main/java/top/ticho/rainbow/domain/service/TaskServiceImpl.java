package top.ticho.rainbow.domain.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.TaskService;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.core.component.SchedulerTemplate;
import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.rainbow.interfaces.assembler.TaskAssembler;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 定时任务调度信息 服务实现
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
    private List<AbstracTask> abstracTasks;

    @Autowired
    private SchedulerTemplate schedulerTemplate;

    // @formatter:off

    @PostConstruct
    public void init() {
        List<Task> tasks = taskRepository.list();
        List<String> jobs = schedulerTemplate.listJobs();
        for (Task task : tasks) {
            String jobId = task.getId().toString();
            if (!jobs.contains(jobId)) {
                schedulerTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
            }
            if (Objects.equals(task.getStatus(), 1)) {
                schedulerTemplate.resumeJob(jobId, DEFAULT_JOB_GROUP);
            } else {
                schedulerTemplate.pauseJob(jobId, DEFAULT_JOB_GROUP);
            }
            jobs.remove(jobId);
        }
        if (!jobs.isEmpty()) {
            jobs.forEach(jobId -> schedulerTemplate.deleteJob(jobId, DEFAULT_JOB_GROUP));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(TaskDTO taskDTO) {
        ValidUtil.valid(taskDTO);
        Task task = TaskAssembler.INSTANCE.dtoToEntity(taskDTO);
        task.setId(CloudIdUtil.getId());
        Assert.isTrue(taskRepository.save(task), BizErrCode.FAIL, "保存失败");
        check(task);
        boolean addedJob = schedulerTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        Assert.isTrue(addedJob, BizErrCode.FAIL, "添加任务失败");
        if (Objects.equals(task.getStatus(), 1)) {
            schedulerTemplate.resumeJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        } else {
            schedulerTemplate.pauseJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        }
    }

    private void check(Task task) {
        boolean present = abstracTasks
            .stream()
            .map(x -> x.getClass().getName())
            .anyMatch(x -> x.equals(task.getContent()));
        Assert.isTrue(present, BizErrCode.FAIL, "执行类不存在");
        boolean valid = SchedulerTemplate.isValid(task.getCronExpression());
        Assert.isTrue(valid, BizErrCode.FAIL, "cron表达式不正确");
        String param = task.getParam();
        if (StrUtil.isNotBlank(param)) {
            boolean json = JsonUtil.isJson(param);
            Assert.isTrue(json, BizErrCode.FAIL, "参数格式不正确");
        }
    }

    @Override
    public void remove(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Assert.isTrue(taskRepository.removeById(id), BizErrCode.FAIL, "删除失败");
        boolean deleteJob = schedulerTemplate.deleteJob(id.toString(), DEFAULT_JOB_GROUP);
        Assert.isTrue(deleteJob, BizErrCode.FAIL, "删除任务失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TaskDTO taskDTO) {
        ValidUtil.valid(taskDTO, ValidGroup.Upd.class);
        Task task = TaskAssembler.INSTANCE.dtoToEntity(taskDTO);
        check(task);
        Assert.isTrue(taskRepository.updateById(task), BizErrCode.FAIL, "修改失败");
        boolean exists = schedulerTemplate.checkExists(task.getId().toString(), DEFAULT_JOB_GROUP);
        if (exists) {
            boolean deleteJob = schedulerTemplate.deleteJob(task.getId().toString(), DEFAULT_JOB_GROUP);
            Assert.isTrue(deleteJob, BizErrCode.FAIL, "删除任务失败");
        }
        Task dbTask = taskRepository.getById(task.getId());
        Integer status = dbTask.getStatus();
        boolean addedJob = schedulerTemplate.addJob(task.getId().toString(), DEFAULT_JOB_GROUP, task.getContent(), task.getCronExpression(), task.getName(), task.getParam());
        Assert.isTrue(addedJob, BizErrCode.FAIL, "添加任务失败");
        if (Objects.equals(status, 1)) {
            schedulerTemplate.resumeJob(task.getId().toString(), DEFAULT_JOB_GROUP);
        }
    }

    @Override
    public void runOnce(Long id, String param) {
        Assert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
        Assert.isNotNull(task, BizErrCode.FAIL, "任务不存在");
        boolean exists = schedulerTemplate.checkExists(task.getId().toString(), DEFAULT_JOB_GROUP);
        Assert.isTrue(exists, BizErrCode.FAIL, "任务不存在");
        boolean runJob = schedulerTemplate.runOnce(task.getId().toString(), DEFAULT_JOB_GROUP, param);
        Assert.isTrue(runJob, BizErrCode.FAIL, "立即执行任务失败");
    }

    @Override
    public void pause(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
        Assert.isNotNull(task, BizErrCode.FAIL, "任务不存在");
        Integer status = task.getStatus();
        Assert.isTrue(Objects.equals(status, 1), BizErrCode.FAIL, "任务未启动");
        boolean exists = schedulerTemplate.checkExists(id.toString(), DEFAULT_JOB_GROUP);
        boolean updated = taskRepository.updateStatusBatch(Collections.singletonList(id), 0);
        Assert.isTrue(updated, BizErrCode.FAIL, "暂停任务失败");
        Assert.isTrue(exists, BizErrCode.FAIL, "任务不存在");
        boolean pauseJob = schedulerTemplate.pauseJob(id.toString(), DEFAULT_JOB_GROUP);
        Assert.isTrue(pauseJob, BizErrCode.FAIL, "暂停任务失败");
    }

    @Override
    public void resume(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
        Assert.isNotNull(task, BizErrCode.FAIL, "任务不存在");
        Integer status = task.getStatus();
        Assert.isTrue(Objects.equals(status, 0), BizErrCode.FAIL, "任务已启动");
        boolean updated = taskRepository.updateStatusBatch(Collections.singletonList(id), 1);
        Assert.isTrue(updated, BizErrCode.FAIL, "恢复任务失败");
        boolean exists = schedulerTemplate.checkExists(id.toString(), DEFAULT_JOB_GROUP);
        Assert.isTrue(exists, BizErrCode.FAIL, "任务不存在");
        boolean resumeJob = schedulerTemplate.resumeJob(id.toString(), DEFAULT_JOB_GROUP);
        Assert.isTrue(resumeJob, BizErrCode.FAIL, "恢复任务失败");
    }

    @Override
    public List<String> getRecentCronTime(String cronExpression, Integer num) {
        num = Optional.ofNullable(num).orElse(10);
        Assert.isTrue(num > 0, BizErrCode.FAIL, "查询数量必须大于0");
        Assert.isTrue(SchedulerTemplate.isValid(cronExpression), BizErrCode.FAIL, "cron表达式不合法");
        return SchedulerTemplate.getRecentCronTime(cronExpression, num);
    }

    @Override
    public TaskDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Task task = taskRepository.getById(id);
        return TaskAssembler.INSTANCE.entityToDto(task);
    }

    @Override
    public PageResult<TaskDTO> page(TaskQuery query) {
        // @formatter:off
        query.checkPage();
        Page<Task> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        taskRepository.list(query);
        List<TaskDTO> taskDTOs = page.getResult()
            .stream()
            .map(TaskAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), taskDTOs);
        // @formatter:on
    }
}
