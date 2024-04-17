package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "客户端信息DTO")
public class ClientDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(value = "主键标识", position = 10)
    private Long id;

    /** 客户端秘钥 */
    @ApiModelProperty(value = "客户端秘钥", position = 20)
    private String accessKey;

    /** 客户端名称 */
    @ApiModelProperty(value = "客户端名称", position = 30)
    @Email(message = "客户端名称请输入邮箱格式")
    @NotBlank(message = "客户端名称不能为空")
    private String name;

    /** 过期时间 */
    @ApiModelProperty(value = "过期时间", position = 35)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;

    /** 状态;1-启用,0-停用 */
    @ApiModelProperty(value = "状态;1-启用,0-停用", position = 40)
    private Integer status;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 50)
    private Integer sort;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 60)
    private String remark;

    /** 连接时间 */
    @ApiModelProperty(value = "连接时间", position = 64)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime connectTime;

    /** 通道状态;1-激活,0-未激活 */
    @ApiModelProperty(value = "通道状态;1-激活,0-未激活", position = 65)
    private Integer channelStatus = 0;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 80)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 90)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 100)
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间", position = 110)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
