package top.ticho.rainbow.application.dto.response;

import lombok.Data;
import top.ticho.rainbow.application.dto.PortDTO;

/**
 * 监控流量数据
 *
 * @author zhajianjun
 * @date 2024-05-16 09:50
 */
@Data
public class FlowMonitorDTO {

    /** 端口号 */
    private Integer port;
    /** 读取流量 */
    private Integer readBytes;
    /** 写入流量 */
    private Integer writeBytes;
    /** 读取消息数 */
    private Integer readMsgs;
    /** 写入消息数 */
    private Integer writeMsgs;
    /** 通道连接数 */
    private Integer channels;
    /** 端口信息 */
    private PortDTO portInfo;

}
