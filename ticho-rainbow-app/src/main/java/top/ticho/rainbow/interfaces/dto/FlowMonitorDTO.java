package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 监控流量数据
 *
 * @author zhajianjun
 * @date 2024-05-16 09:50
 */
@Data
@ApiModel(value = "监控流量数据")
public class FlowMonitorDTO {

    /** 端口号 */
    @ApiModelProperty(value = "端口号", position = 10)
    private Integer port;

    /** 读取流量 */
    @ApiModelProperty(value = "读取流量", position = 20)
    private Integer readBytes;

    /** 写入流量 */
    @ApiModelProperty(value = "写入流量", position = 30)
    private Integer writeBytes;

    /** 读取消息数 */
    @ApiModelProperty(value = "读取消息数", position = 40)
    private Integer readMsgs;

    /** 写入消息数 */
    @ApiModelProperty(value = "写入消息数", position = 50)
    private Integer writeMsgs;

    /** 通道连接数 */
    @ApiModelProperty(value = "通道连接数", position = 60)
    private Integer channels;

    /** 端口信息 */
    @ApiModelProperty(value = "端口信息", position = 70)
    private PortDTO portInfo;

}
