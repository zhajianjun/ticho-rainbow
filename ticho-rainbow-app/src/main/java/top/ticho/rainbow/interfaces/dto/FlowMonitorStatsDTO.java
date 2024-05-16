package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "监控流量数据统计")
public class FlowMonitorStatsDTO {

    /** 端口总数 */
    @ApiModelProperty(value = "客户端数", position = 10)
    private Integer clients;

    /** 端口总数 */
    @ApiModelProperty(value = "激活客户端数", position = 20)
    private Integer activeClients;

    /** 端口总数 */
    @ApiModelProperty(value = "端口数", position = 30)
    private Integer ports;

    /** 激活端口总数 */
    @ApiModelProperty(value = "激活端口数", position = 40)
    private Integer activePorts;

    /** 统计时间 */
    @ApiModelProperty(value = "统计时间", position = 50)
    private LocalDateTime dateTime;

    /** 激活端口流量明细 */
    @ApiModelProperty(value = "激活端口流量明细", position = 60)
    private List<FlowMonitorDTO> flowDetails;

}
