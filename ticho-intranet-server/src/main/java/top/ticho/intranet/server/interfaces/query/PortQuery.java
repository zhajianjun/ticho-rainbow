package top.ticho.intranet.server.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ticho.boot.view.core.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 端口信息查询条件
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "端口信息查询条件")
public class PortQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(value = "主键标识", position = 10)
    private Long id;

    /** 客户端秘钥 */
    @ApiModelProperty(value = "客户端秘钥", position = 20)
    private String accessKey;

    /** 主机端口 */
    @ApiModelProperty(value = "主机端口", position = 30)
    private Integer port;

    /** 客户端地址 */
    @ApiModelProperty(value = "客户端地址", position = 40)
    private String endpoint;

    /** 域名 */
    @ApiModelProperty(value = "域名", position = 50)
    private String domain;

    /** 是否开启;1-开启,0-关闭 */
    @ApiModelProperty(value = "是否开启;1-开启,0-关闭", position = 60)
    private Integer enabled;

    /** 是否永久 */
    @ApiModelProperty(value = "是否永久", position = 70)
    private Integer forever;

    /** 过期时间 */
    @ApiModelProperty(value = "过期时间", position = 80)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;

    /** 协议类型 */
    @ApiModelProperty(value = "协议类型", position = 90)
    private Integer type;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 100)
    private Integer sort;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 110)
    private String remark;

    /** 乐观锁;控制版本更改 */
    @ApiModelProperty(value = "乐观锁;控制版本更改", position = 120)
    private Long version;

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

    /** 删除标识;0-未删除,1-已删除 */
    @ApiModelProperty(value = "删除标识;0-未删除,1-已删除", position = 170)
    private Integer isDelete;

}
