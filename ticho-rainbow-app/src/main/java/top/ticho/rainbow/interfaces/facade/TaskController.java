package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.TaskService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.interfaces.dto.command.TaskModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.TaskRunOnceCommand;
import top.ticho.rainbow.interfaces.dto.command.TaskSaveCommand;
import top.ticho.rainbow.interfaces.dto.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.query.TaskQuery;
import top.ticho.rainbow.interfaces.dto.response.TaskDTO;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.List;


/**
 * 计划任务
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;

    /**
     * 保存计划任务
     */
    @ApiLog("保存计划任务")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody TaskSaveCommand taskSaveCommand) {
        taskService.save(taskSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除计划任务
     */
    @ApiLog("删除计划任务")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        taskService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改计划任务
     */
    @ApiLog("修改计划任务")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody TaskModifyCommand taskModifyCommand) {
        taskService.modify(taskModifyCommand);
        return TiResult.ok();
    }

    /**
     * 启用计划任务
     */
    @ApiLog("启用计划任务")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "角色信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        taskService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用计划任务
     */
    @ApiLog("禁用计划任务")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "角色信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        taskService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 执行一次计划任务
     */
    @ApiLog("执行一次计划任务")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_RUN_ONCE + "')")
    @PostMapping("run-once")
    public TiResult<Void> runOnce(@Validated @RequestBody TaskRunOnceCommand taskRunOnceCommand) {
        taskService.runOnce(taskRunOnceCommand);
        return TiResult.ok();
    }

    /**
     * 根据cron表达式获取近n次的执行时间
     *
     * @param cronExpression cron表达式
     * @param num            查询数量
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_RECENT_CRON_TIME + "')")
    @GetMapping("recent-cron-time")
    public TiResult<List<String>> recentCronTime(String cronExpression, Integer num) {
        return TiResult.ok(taskService.recentCronTime(cronExpression, num));
    }

    /**
     * 查询计划任务(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<TaskDTO>> page(@Validated TaskQuery query) {
        return TiResult.ok(taskService.page(query));
    }

    /**
     * 查询所有计划任务
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_ALL + "')")
    @GetMapping("all")
    public TiResult<List<TaskDTO>> all() {
        return TiResult.ok(taskService.all());
    }

    /**
     * 导出计划任务
     */
    @ApiLog("导出计划任务")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated TaskQuery query) throws IOException {
        taskService.exportExcel(query);
    }

}
