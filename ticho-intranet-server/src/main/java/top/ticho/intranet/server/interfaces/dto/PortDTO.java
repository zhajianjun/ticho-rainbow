package top.ticho.intranet.server.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import top.ticho.boot.web.util.valid.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 端口信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "端口信息DTO")
public class PortDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(value = "主键标识", position = 10)
    @NotNull(message = "编号不能为空", groups = ValidGroup.Upd.class)
    private Long id;

    /** 客户端秘钥 */
    @ApiModelProperty(value = "客户端秘钥", position = 20)
    @NotBlank(message = "客户端秘钥不能为空")
    private String accessKey;

    /** 主机端口 */
    @ApiModelProperty(value = "主机端口", position = 30)
    @NotNull(message = "主机端口不能为空")
    @Max(message = "主机端口不大于65535", value = 65535)
    @Min(message = "主机端口不小于1", value = 1)
    private Integer port;

    /** 客户端地址 */
    @ApiModelProperty(value = "客户端地址", position = 40)
    @NotBlank(message = "客户端地址不能为空")
    @Pattern(message = "客户端地址格式不正确[ip:port]", regexp = "\\b(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-5][0-5][0-3][0-5])\\b")
    private String endpoint;

    /** 域名 */
    @ApiModelProperty(value = "域名", position = 50)
    @Pattern(message = "域名格式不正确", regexp = "^([a-z0-9-]+\\.)+[a-z]{2,}(/\\S*)?$")
    private String domain;

    /** 是否开启;1-开启,0-关闭 */
    @ApiModelProperty(value = "是否开启;1-开启,0-关闭", position = 60)
    private Integer enabled;

    /** 是否永久;1-是,0-否 */
    @ApiModelProperty(value = "是否永久;1-是,0-否", position = 70)
    private Integer forever;

    /** 过期时间 */
    @ApiModelProperty(value = "过期时间", position = 80)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;

    /** 协议类型 */
    @ApiModelProperty(value = "协议类型", position = 90)
    @NotNull(message = "协议类型不能为空")
    private Integer type;

    /** 通道状态;1-激活,0-未激活 */
    @ApiModelProperty(value = "通道状态;1-激活,0-未激活", position = 95)
    private Integer channelStatus;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 100)
    private Integer sort;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 110)
    private String remark;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 130)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 140)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 150)
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间", position = 160)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
