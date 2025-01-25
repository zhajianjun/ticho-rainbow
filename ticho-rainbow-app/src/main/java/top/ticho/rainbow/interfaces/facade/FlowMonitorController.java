package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.intranet.service.FlowMonitorService;
import top.ticho.rainbow.interfaces.dto.FlowMonitorStatsDTO;
import top.ticho.starter.view.core.TiResult;

import javax.annotation.Resource;

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

    @Resource
    private FlowMonitorService flowMonitorService;

    @PreAuthorize("@perm.hasPerms('intranet:flow-monitor:info')")
    @ApiOperation(value = "查询流量监控")
    @ApiOperationSupport(order = 10)
    @GetMapping("info")
    public TiResult<FlowMonitorStatsDTO> info() {
        return TiResult.ok(flowMonitorService.info());
    }

}
