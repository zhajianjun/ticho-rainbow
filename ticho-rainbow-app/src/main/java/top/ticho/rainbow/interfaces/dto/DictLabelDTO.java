package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.web.util.valid.TiValidGroup;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典标签DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "字典标签DTO")
public class DictLabelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    @NotNull(message = "字典编号不能为空", groups = {TiValidGroup.Upd.class})
    private Long id;

    /** 字典编码 */
    @ApiModelProperty(value = "字典编码", position = 30)
    @NotBlank(message = "字典编码不能为空", groups = {TiValidGroup.Add.class})
    private String code;

    /** 字典标签 */
    @ApiModelProperty(value = "字典标签", position = 50)
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 50, message = "字典标签最大不能超过50个字符")
    private String label;

    /** 字典值 */
    @ApiModelProperty(value = "字典值", position = 40)
    @NotBlank(message = "字典值不能为空")
    @Size(max = 50, message = "字典值最大不能超过50个字符")
    private String value;

    /** 图标 */
    @ApiModelProperty(value = "图标", position = 42)
    private String icon;

    /** 颜色 */
    @ApiModelProperty(value = "颜色", position = 44)
    private String color;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 60)
    @Max(value = 65535, message = "排序最大值为65535")
    private Integer sort;

    /** 状态;1-启用,0-停用 */
    @ApiModelProperty(value = "状态;1-启用,0-停用", position = 90)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 100)
    @Size(max = 1024, message = "备注信息最大不能超过1024个字符")
    private String remark;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 110)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
