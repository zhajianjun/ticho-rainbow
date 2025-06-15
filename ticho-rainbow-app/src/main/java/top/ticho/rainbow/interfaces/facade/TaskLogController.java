package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.TaskLogService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;
import top.ticho.rainbow.interfaces.dto.TaskLogDTO;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import java.io.IOException;


/**
 * 计划任务日志
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("task-log")
public class TaskLogController {
    private final TaskLogService taskLogService;


    /**
     * 查询计划任务日志(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_LOG_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<TaskLogDTO>> page(@Validated TaskLogQuery query) {
        return TiResult.ok(taskLogService.page(query));
    }

    /**
     * 导出计划任务日志
     */
    @ApiLog("导出计划任务日志")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_TASK_LOG_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated TaskLogQuery query) throws IOException {
        taskLogService.exportExcel(query);
    }

}
