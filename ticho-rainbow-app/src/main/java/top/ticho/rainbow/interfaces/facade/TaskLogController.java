package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.rainbow.application.service.TaskLogService;
import top.ticho.rainbow.interfaces.dto.TaskLogDTO;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;


/**
 * 计划任务日志信息
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@RestController
@RequestMapping("taskLog")
@Api(tags = "计划任务日志信息")
@ApiSort(160)
public class TaskLogController {

    @Autowired
    private TaskLogService taskLogService;

    @PreAuthorize("@perm.hasPerms('system:taskLog:getById')")
    @ApiOperation(value = "主键查询计划任务日志信息")
    @ApiOperationSupport(order = 10)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<TaskLogDTO> getById(Long id) {
        return Result.ok(taskLogService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:taskLog:page')")
    @ApiOperation(value = "分页查询计划任务日志信息")
    @ApiOperationSupport(order = 20)
    @GetMapping("page")
    public Result<PageResult<TaskLogDTO>> page(TaskLogQuery query) {
        return Result.ok(taskLogService.page(query));
    }

}
