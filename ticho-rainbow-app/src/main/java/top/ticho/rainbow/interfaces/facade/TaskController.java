package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.rainbow.application.service.TaskService;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import java.util.List;


/**
 * 定时任务调度
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@RestController
@RequestMapping("task")
@Api(tags = "定时任务调度")
@ApiSort(140)
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("@perm.hasPerms('system:task:save')")
    @ApiOperation(value = "保存定时任务调度")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:remove')")
    @ApiOperation(value = "删除定时任务调度")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> remove(Long id) {
        taskService.remove(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:update')")
    @ApiOperation(value = "修改定时任务调度")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:runOnce')")
    @ApiOperation(value = "执行一次定时任务调度")
    @ApiOperationSupport(order = 40)
    @GetMapping("runOnce")
    public Result<Void> runOnce(Long id, String param) {
        taskService.runOnce(id, param);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:pause')")
    @ApiOperation(value = "暂停定时任务调度")
    @ApiOperationSupport(order = 50)
    @GetMapping("pause")
    public Result<Void> pause(Long id) {
        taskService.pause(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:resume')")
    @ApiOperation(value = "恢复定时任务调度")
    @ApiOperationSupport(order = 60)
    @GetMapping("resume")
    public Result<Void> resume(Long id) {
        taskService.resume(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:getRecentCronTime')")
    @ApiOperation(value = "根据cron表达式获取近n次的执行时间")
    @ApiOperationSupport(order = 70)
    @ApiImplicitParam(value = "编号", name = "cronExpression", required = true)
    @GetMapping("getRecentCronTime")
    public Result<List<String>> getRecentCronTime(String cronExpression, Integer num) {
        return Result.ok(taskService.getRecentCronTime(cronExpression, num));
    }

    @PreAuthorize("@perm.hasPerms('system:task:getById')")
    @ApiOperation(value = "主键查询定时任务调度")
    @ApiOperationSupport(order = 80)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<TaskDTO> getById(Long id) {
        return Result.ok(taskService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:task:page')")
    @ApiOperation(value = "分页查询定时任务调度")
    @ApiOperationSupport(order = 90)
    @GetMapping("page")
    public Result<PageResult<TaskDTO>> page(TaskQuery query) {
        return Result.ok(taskService.page(query));
    }

}
