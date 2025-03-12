package top.ticho.rainbow.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.ticho.starter.web.util.valid.TiValidGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotNull(message = "编号不能为空", groups = TiValidGroup.Upd.class)
    private Long id;
    /** 客户端秘钥 */
    @NotBlank(message = "客户端秘钥不能为空")
    private String accessKey;
    /** 主机端口 */
    @NotNull(message = "主机端口不能为空")
    @Max(message = "主机端口不大于65535", value = 65535)
    @Min(message = "主机端口不小于1", value = 1)
    private Integer port;
    /** 客户端地址 */
    @NotBlank(message = "客户端地址不能为空")
    @Pattern(message = "客户端地址格式不正确[ip:port]", regexp = "\\b(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-5][0-5][0-3][0-5])\\b")
    private String endpoint;
    /** 域名 */
    private String domain;
    /** 状态;1-启用,0-停用 */
    @NotNull(message = "是否开启不能为空")
    private Integer status;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime expireAt;
    /** 协议类型 */
    @NotNull(message = "协议类型不能为空")
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
