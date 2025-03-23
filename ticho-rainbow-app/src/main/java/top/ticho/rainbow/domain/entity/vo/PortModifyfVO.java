package top.ticho.rainbow.domain.entity.vo;

import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 端口信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Value
public class PortModifyfVO {

    /** 客户端秘钥 */
    String accessKey;
    /** 主机端口 */
    Integer port;
    /** 客户端地址 */
    String endpoint;
    /** 域名 */
    String domain;
    /** 状态;1-启用,0-停用 */
    Integer status;
    /** 过期时间 */
    LocalDateTime expireAt;
    /** 协议类型 */
    Integer type;
    /** 排序 */
    Integer sort;
    /** 备注信息 */
    String remark;
    /** 版本号 */
    Long version;

}
