package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.application.service.TaskLogService;
import top.ticho.rainbow.interfaces.dto.TaskLogDTO;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;

import java.io.IOException;


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
    @ApiOperation(value = "查询计划任务日志")
    @ApiOperationSupport(order = 10)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<TaskLogDTO> getById(Long id) {
        return Result.ok(taskLogService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:taskLog:page')")
    @ApiOperation(value = "查询计划任务日志(分页)")
    @ApiOperationSupport(order = 20)
    @PostMapping("page")
    public Result<PageResult<TaskLogDTO>> page(@RequestBody TaskLogQuery query) {
        return Result.ok(taskLogService.page(query));
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:taskLog:expExcel')")
    @ApiOperation(value = "导出计划任务日志", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 110)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody TaskLogQuery query) throws IOException {
        taskLogService.expExcel(query);
    }

}
