package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.TaskLogDTO;
import top.ticho.rainbow.application.dto.query.TaskLogQuery;
import top.ticho.rainbow.application.service.TaskLogService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import java.io.IOException;


/**
 * 计划任务日志信息
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("taskLog")
public class TaskLogController {
    private final TaskLogService taskLogService;
    /**
     * 查询计划任务日志
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:taskLog:getById')")
    @GetMapping("{id}")
    public TiResult<TaskLogDTO> getById(@PathVariable("id") Long id) {
        return TiResult.ok(taskLogService.getById(id));
    }

    /**
     * 查询计划任务日志(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:taskLog:page')")
    @GetMapping
    public TiResult<TiPageResult<TaskLogDTO>> page(@RequestBody TaskLogQuery query) {
        return TiResult.ok(taskLogService.page(query));
    }

    /**
     * 导出计划任务日志
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:taskLog:expExcel')")
    @GetMapping("expExcel")
    public void expExcel(@RequestBody TaskLogQuery query) throws IOException {
        taskLogService.expExcel(query);
    }

}
