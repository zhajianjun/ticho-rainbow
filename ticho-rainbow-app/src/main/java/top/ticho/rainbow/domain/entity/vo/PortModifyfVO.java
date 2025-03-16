package top.ticho.rainbow.domain.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 端口信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
public class PortModifyfVO {

    /** 客户端秘钥 */
    private String accessKey;
    /** 主机端口 */
    private Integer port;
    /** 客户端地址 */
    private String endpoint;
    /** 域名 */
    private String domain;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 过期时间 */
    private LocalDateTime expireAt;
    /** 协议类型 */
    private Integer type;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;

}
