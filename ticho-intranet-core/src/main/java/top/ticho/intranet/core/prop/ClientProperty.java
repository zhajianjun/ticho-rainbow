package top.ticho.intranet.core.prop;

import lombok.Data;

/**
 * 客户端配置
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class ClientProperty {

    /** 秘钥 */
    private String accessKey;
    /** 服务地址 */
    private String serverHost;
    /** 服务端口 */
    private Integer serverPort;
    /** 服务是否开启ssl */
    private Boolean sslEnable;
    /** ssl证书路径 */
    private String sslPath;
    /** ssl证书密码 */
    private String sslPassword;
    /** 最大线程数量 */
    private Integer maxPoolSize = 100;

}
