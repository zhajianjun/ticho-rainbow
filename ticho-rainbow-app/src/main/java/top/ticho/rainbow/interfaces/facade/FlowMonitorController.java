package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.Result;
import top.ticho.rainbow.application.service.FlowMonitorService;
import top.ticho.rainbow.interfaces.dto.FlowMonitorDTO;
import top.ticho.rainbow.interfaces.dto.FlowMonitorStatsDTO;

import java.util.List;

/**
 * 流量监控
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@RestController
@RequestMapping("flow-monitor")
@Api(tags = "流量监控")
@ApiSort(100)
public class FlowMonitorController {

    @Autowired
    private FlowMonitorService flowMonitorService;

    @PreAuthorize("@perm.hasPerms('intranet:flow-monitor:info')")
    @ApiOperation(value = "查询流量监控")
    @ApiOperationSupport(order = 10)
    @GetMapping("info")
    public Result<FlowMonitorStatsDTO> info() {
        return Result.ok(flowMonitorService.info());
    }

}
