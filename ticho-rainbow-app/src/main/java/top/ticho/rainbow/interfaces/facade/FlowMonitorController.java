package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.response.FlowMonitorStatsDTO;
import top.ticho.rainbow.application.service.FlowMonitorService;
import top.ticho.starter.view.core.TiResult;

/**
 * 流量监控
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("flow-monitor")
public class FlowMonitorController {

    private final FlowMonitorService flowMonitorService;
    /**
     * 查询流量监控
     */
    @PreAuthorize("@perm.hasPerms('intranet:flow-monitor:info')")
    @GetMapping("info")
    public TiResult<FlowMonitorStatsDTO> info() {
        return TiResult.ok(flowMonitorService.info());
    }

}
