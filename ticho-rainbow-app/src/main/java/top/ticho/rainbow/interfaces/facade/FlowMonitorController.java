package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.FlowMonitorService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.interfaces.dto.response.FlowMonitorStatsDTO;
import top.ticho.starter.view.core.TiResult;

/**
 * 内网穿透流量信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("flow-monitor")
public class FlowMonitorController {

    private final FlowMonitorService flowMonitorService;

    /**
     * 查询流量
     */
    @ApiLog("查询流量信息")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_FLOW_MONITOR_INFO + "')")
    @GetMapping("info")
    public TiResult<FlowMonitorStatsDTO> info() {
        return TiResult.ok(flowMonitorService.info());
    }

}
