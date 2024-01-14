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

    /** 字典类型id */
    @ApiModelProperty(value = "字典类型编码", position = 30)
    @NotNull(message = "字典类型编码不能为空", groups = {ValidGroup.Add.class})
    private String code;

    /** 字典名称 */
    @ApiModelProperty(value = "字典标签", position = 50)
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 50, message = "字典标签最大不能超过50个字符")
    private String label;

    /** 字典编码 */
    @ApiModelProperty(value = "字典值", position = 40)
    @NotBlank(message = "字典值不能为空")
    @Size(max = 50, message = "字典值最大不能超过50个字符")
    private String value;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 60)
    @Max(value = 65535, message = "排序最大值为65535")
    private Integer sort;

    /** 状态;1-正常,0-停用 */
    @ApiModelProperty(value = "状态;1-正常,0-停用", position = 90)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 100)
    @Size(max = 1024, message = "备注信息最大不能超过1024个字符")
    private String remark;

}
