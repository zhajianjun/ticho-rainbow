package top.ticho.rainbow.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 端口信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
public class PortDTO {

    /** 主键标识 */
    private Long id;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;
    /** 协议类型 */
    private Integer type;
    /** 客户端状态;1-激活,0-未激活 */
    private Integer clientChannelStatus = 0;
    /** 通道状态;1-激活,0-未激活 */
    private Integer appChannelStatus = 0;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
