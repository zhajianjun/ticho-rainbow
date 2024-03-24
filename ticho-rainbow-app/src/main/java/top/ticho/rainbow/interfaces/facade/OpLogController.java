package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.rainbow.application.service.OpLogService;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;


/**
 * 日志信息
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@RestController
@RequestMapping("opLog")
@Api(tags = "日志信息")
@ApiSort(150)
public class OpLogController {

    @Autowired
    private OpLogService opLogService;

    @ApiOperation(value = "主键查询日志信息")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<OpLogDTO> getById(Long id) {
        return Result.ok(opLogService.getById(id));
    }

    @ApiOperation(value = "分页查询日志信息")
    @ApiOperationSupport(order = 50)
    @GetMapping("page")
    public Result<PageResult<OpLogDTO>> page(OpLogQuery query) {
        return Result.ok(opLogService.page(query));
    }

}
