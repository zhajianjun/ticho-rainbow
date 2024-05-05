package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

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

    /** 过期时间 */
    @ApiModelProperty(value = "过期时间", position = 60)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;

    /** 协议类型 */
    @ApiModelProperty(value = "协议类型", position = 90)
    private Integer type;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 110)
    private String remark;

}
