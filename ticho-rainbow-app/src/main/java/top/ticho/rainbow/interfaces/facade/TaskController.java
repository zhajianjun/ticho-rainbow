package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.command.TaskModifyCommand;
import top.ticho.rainbow.application.dto.command.TaskSaveCommand;
import top.ticho.rainbow.application.dto.query.TaskQuery;
import top.ticho.rainbow.application.dto.response.TaskDTO;
import top.ticho.rainbow.application.service.TaskService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;


/**
 * 定时任务调度
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;

    /**
     * 保存计划任务
     */
    @PreAuthorize("@perm.hasPerms('system:task:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody TaskSaveCommand taskSaveCommand) {
        taskService.save(taskSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除计划任务
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:task:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        taskService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改计划任务
     */
    @PreAuthorize("@perm.hasPerms('system:task:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody TaskModifyCommand taskModifyCommand) {
        taskService.modify(taskModifyCommand);
        return TiResult.ok();
    }

    /**
     * 执行一次计划任务
     *
     * @param id    编号
     * @param param 参数
     */
    @PreAuthorize("@perm.hasPerms('system:task:runOnce')")
    @GetMapping("runOnce")
    public TiResult<Void> runOnce(@NotNull(message = "编号不能为空") Long id, String param) {
        taskService.runOnce(id, param);
        return TiResult.ok();
    }

    /**
     * 暂停计划任务
     *
     * @param id 编号
     * @return {@link TiResult }<{@link Void }>
     */
    @PreAuthorize("@perm.hasPerms('system:task:pause')")
    @GetMapping("pause")
    public TiResult<Void> pause(@NotNull(message = "编号不能为空") Long id) {
        taskService.pause(id);
        return TiResult.ok();
    }

    /**
     * 恢复计划任务
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:task:resume')")
    @GetMapping("resume")
    public TiResult<Void> resume(@NotNull(message = "编号不能为空") Long id) {
        taskService.resume(id);
        return TiResult.ok();
    }

    /**
     * 根据cron表达式获取近n次的执行时间
     *
     * @param cronExpression cron表达式
     * @param num            查询数量
     */
    @PreAuthorize("@perm.hasPerms('system:task:getRecentCronTime')")
    @GetMapping("getRecentCronTime")
    public TiResult<List<String>> getRecentCronTime(String cronExpression, Integer num) {
        return TiResult.ok(taskService.getRecentCronTime(cronExpression, num));
    }

    /**
     * 查询计划任务
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:task:getById')")
    @GetMapping()
    public TiResult<TaskDTO> getById(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(taskService.getById(id));
    }

    /**
     * 查询所有计划任务(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:task:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<TaskDTO>> page(@Validated @RequestBody TaskQuery query) {
        return TiResult.ok(taskService.page(query));
    }

    /**
     * 查询所有计划任务
     */
    @PreAuthorize("@perm.hasPerms('system:task:all')")
    @GetMapping("all")
    public TiResult<List<TaskDTO>> list(@Validated TaskQuery query) {
        return TiResult.ok(taskService.list(query));
    }

    /**
     * 导出计划任务
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:task:expExcel')")
    @GetMapping("excel/export")
    public void expExcel(@Validated @RequestBody TaskQuery query) throws IOException {
        taskService.expExcel(query);
    }

}
