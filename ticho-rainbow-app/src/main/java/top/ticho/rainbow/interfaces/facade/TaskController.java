package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.task.TaskService;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.rainbow.interfaces.query.TaskQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


/**
 * 定时任务调度
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@RestController
@RequestMapping("task")
@Api(tags = "计划任务")
@ApiSort(140)
public class TaskController {

    @Resource
    private TaskService taskService;

    @PreAuthorize("@perm.hasPerms('system:task:save')")
    @ApiOperation(value = "保存计划任务")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody TaskDTO taskDTO) {
        taskService.save(taskDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:remove')")
    @ApiOperation(value = "删除计划任务")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> remove(Long id) {
        taskService.remove(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:update')")
    @ApiOperation(value = "修改计划任务")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:runOnce')")
    @ApiOperation(value = "执行一次计划任务")
    @ApiOperationSupport(order = 40)
    @GetMapping("runOnce")
    public TiResult<Void> runOnce(Long id, String param) {
        taskService.runOnce(id, param);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:pause')")
    @ApiOperation(value = "暂停计划任务")
    @ApiOperationSupport(order = 50)
    @GetMapping("pause")
    public TiResult<Void> pause(Long id) {
        taskService.pause(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:resume')")
    @ApiOperation(value = "恢复计划任务")
    @ApiOperationSupport(order = 60)
    @GetMapping("resume")
    public TiResult<Void> resume(Long id) {
        taskService.resume(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:task:getRecentCronTime')")
    @ApiOperation(value = "根据cron表达式获取近n次的执行时间")
    @ApiOperationSupport(order = 70)
    @ApiImplicitParam(value = "编号", name = "cronExpression", required = true)
    @GetMapping("getRecentCronTime")
    public TiResult<List<String>> getRecentCronTime(String cronExpression, Integer num) {
        return TiResult.ok(taskService.getRecentCronTime(cronExpression, num));
    }

    @PreAuthorize("@perm.hasPerms('system:task:getById')")
    @ApiOperation(value = "查询计划任务")
    @ApiOperationSupport(order = 80)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<TaskDTO> getById(Long id) {
        return TiResult.ok(taskService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:task:page')")
    @ApiOperation(value = "查询所有计划任务(分页)")
    @ApiOperationSupport(order = 90)
    @PostMapping("page")
    public TiResult<TiPageResult<TaskDTO>> page(@RequestBody TaskQuery query) {
        return TiResult.ok(taskService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:task:list')")
    @ApiOperation(value = "查询所有计划任务")
    @ApiOperationSupport(order = 100)
    @GetMapping("list")
    public TiResult<List<TaskDTO>> list(TaskQuery query) {
        return TiResult.ok(taskService.list(query));
    }

    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:task:expExcel')")
    @ApiOperation(value = "导出计划任务", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 110)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody TaskQuery query) throws IOException {
        taskService.expExcel(query);
    }

}
