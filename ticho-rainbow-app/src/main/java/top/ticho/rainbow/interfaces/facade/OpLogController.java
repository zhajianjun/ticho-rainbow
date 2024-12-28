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
import top.ticho.boot.view.core.TiPageResult;
import top.ticho.boot.view.core.TiResult;
import top.ticho.rainbow.application.system.service.OpLogService;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;

import java.io.IOException;


/**
 * 日志信息
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@RestController
@RequestMapping("opLog")
@Api(tags = "操作日志")
@ApiSort(150)
public class OpLogController {

    @Autowired
    private OpLogService opLogService;

    @PreAuthorize("@perm.hasPerms('system:opLog:getById')")
    @ApiOperation(value = "查询操作日志")
    @ApiOperationSupport(order = 10)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<OpLogDTO> getById(Long id) {
        return TiResult.ok(opLogService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:opLog:page')")
    @ApiOperation(value = "查询全部操作日志(分页)")
    @ApiOperationSupport(order = 20)
    @PostMapping("page")
    public TiResult<TiPageResult<OpLogDTO>> page(@RequestBody OpLogQuery query) {
        return TiResult.ok(opLogService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:opLog:expExcel')")
    @ApiOperation(value = "导出操作日志", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 30)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody OpLogQuery query) throws IOException {
        opLogService.expExcel(query);
    }

}
