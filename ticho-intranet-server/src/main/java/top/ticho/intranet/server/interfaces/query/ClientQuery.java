package top.ticho.intranet.server.interfaces.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

import java.io.Serializable;

/**
 * 客户端信息查询条件
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "客户端信息查询条件")
public class ClientQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 客户端秘钥 */
    @ApiModelProperty(value = "客户端秘钥", position = 20)
    private String accessKey;

    /** 客户端名称 */
    @ApiModelProperty(value = "客户端名称", position = 30)
    private String name;

    /** 状态;1-启用,0-停用 */
    @ApiModelProperty(value = "状态;1-启用,0-停用", position = 40)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 60)
    private String remark;

}
