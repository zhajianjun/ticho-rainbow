package top.ticho.rainbow.application.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 监控流量数据统计
 *
 * @author zhajianjun
 * @date 2024-05-16 10:29
 */
@Data
public class FlowMonitorStatsDTO {

    /** 端口总数 */
    private Integer clients;
    /** 端口总数 */
    private Integer activeClients;
    /** 端口总数 */
    private Integer ports;
    /** 激活端口总数 */
    private Integer activePorts;
    /** 统计时间 */
    private LocalDateTime dateTime;
    /** 激活端口流量明细 */
    private List<FlowMonitorDTO> flowDetails;

}
