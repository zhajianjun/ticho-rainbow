package top.ticho.rainbow.interfaces.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.io.Serializable;

/**
 * 字典标签查询条件
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "字典标签查询条件")
public class DictLabelQuery extends TiPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 字典编码 */
    @ApiModelProperty(value = "字典编码", position = 20)
    private String code;

    /** 字典标签 */
    @ApiModelProperty(value = "字典标签", position = 30)
    private String label;

    /** 字典值 */
    @ApiModelProperty(value = "字典值", position = 40)
    private String value;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 50)
    private Integer sort;

    /** 状态;1-启用,0-停用 */
    @ApiModelProperty(value = "状态;1-启用,0-停用", position = 60)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 70)
    private String remark;

}
