package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.query.TaskLogQuery;
import top.ticho.rainbow.application.dto.response.TaskLogDTO;
import top.ticho.rainbow.application.service.TaskLogService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;


/**
 * 计划任务日志
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("task-log")
public class TaskLogController {
    private final TaskLogService taskLogService;

    /**
     * 查询计划任务日志
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:task-log:find')")
    @GetMapping
    public TiResult<TaskLogDTO> find(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(taskLogService.find(id));
    }

    /**
     * 查询计划任务日志(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:task-log:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<TaskLogDTO>> page(@Validated TaskLogQuery query) {
        return TiResult.ok(taskLogService.page(query));
    }

    /**
     * 导出计划任务日志
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:task-log:export')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated TaskLogQuery query) throws IOException {
        taskLogService.exportExcel(query);
    }

}
