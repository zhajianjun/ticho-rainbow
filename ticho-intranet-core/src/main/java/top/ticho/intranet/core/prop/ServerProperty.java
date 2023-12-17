package top.ticho.intranet.core.prop;

import lombok.Data;

/**
 * 服务配置
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class ServerProperty {

    /** 端口 */
    private Integer port;
    /** 是否开启ssl */
    private Boolean sslEnable;
    /** ssl端口 */
    private Integer sslPort;
    /** ssl证书路径 */
    private String sslPath;
    /** ssl证书密码 */
    private String sslPassword;
    /** 最大请求数 */
    private Long maxRequests = 1024L;
    /** 最多绑定端口数 */
    private Long maxBindPorts = 10000L;

}
