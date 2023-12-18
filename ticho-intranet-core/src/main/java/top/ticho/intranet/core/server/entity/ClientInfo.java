package top.ticho.intranet.core.server.entity;

import io.netty.channel.Channel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 通道信息
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class ClientInfo implements Serializable {
    private static final long serialVersionUID = 2788298692197340477L;

    /** 客户端秘钥 */
    private String accessKey;

    /** 客户端名称 */
    private String name;

    /** 是否开启;1-开启,0-关闭 */
    private Integer enabled;

    /** 状态 */
    private Integer status;

    /** 连接时间 */
    private LocalDateTime connectTime;

    /** 连接的通道信息 */
    private transient Channel channel;

    /** 端口信息(端口号, 端口对象信息) */
    private Map<Integer, PortInfo> portMap;

}