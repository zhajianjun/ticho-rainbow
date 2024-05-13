package top.ticho.rainbow.interfaces.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息查询条件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "角色信息查询条件")
public class RoleQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号列表 */
    @ApiModelProperty(value = "主键编号列表", position = 9)
    private List<Long> ids;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 角色编码 */
    @ApiModelProperty(value = "角色编码", position = 20)
    private String code;

    /** 角色名称 */
    @ApiModelProperty(value = "角色名称", position = 30)
    private String name;

    /** 状态;1-正常,0-禁用 */
    @ApiModelProperty(value = "状态;1-正常,0-禁用", position = 35)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 50)
    private String remark;

}
