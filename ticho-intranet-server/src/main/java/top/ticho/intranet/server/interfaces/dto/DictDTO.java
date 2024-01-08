package top.ticho.intranet.server.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.web.util.valid.ValidGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 数据字典DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "数据字典DTO")
public class DictDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @ApiModelProperty(value = "主键编号", position = 10)
    @NotBlank(message = "字典编号不能为空", groups = {ValidGroup.Upd.class})
    private Long id;

    /** 父id */
    @ApiModelProperty(value = "父id", position = 20)
    private Long parentId;

    /** 字典类型id */
    @ApiModelProperty(value = "字典类型id", position = 30)
    @NotNull(message = "字典类型编号不能为空", groups = {ValidGroup.Add.class})
    private Long typeId;

    /** 字典编码 */
    @ApiModelProperty(value = "字典编码", position = 40)
    @Size(max = 50, message = "字典编码最大不能超过50个字符")
    private String code;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称", position = 50)
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 50, message = "字典名称最大不能超过50个字符")
    private String name;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 60)
    @Max(value = 65535, message = "排序最大值为65535")
    private Integer sort;

    /** 层级 */
    @ApiModelProperty(value = "层级", position = 70)
    private Integer level;

    /** 结构 */
    @ApiModelProperty(value = "结构", position = 80)
    private String structure;

    /** 状态;0-禁用,1-正常 */
    @ApiModelProperty(value = "状态;0-禁用,1-正常", position = 90)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 100)
    @Size(max = 1024, message = "备注信息最大不能超过1024个字符")
    private String remark;

}
