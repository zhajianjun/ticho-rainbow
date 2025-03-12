package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 字典标签创建命令
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class DictLabelSaveCommand {

    /** 字典编码 */
    @NotBlank(message = "字典编码不能为空")
    private String code;
    /** 字典标签 */
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 50, message = "字典标签最大不能超过50个字符")
    private String label;
    /** 字典值 */
    @NotBlank(message = "字典值不能为空")
    @Size(max = 50, message = "字典值最大不能超过{max}个字符")
    private String value;
    /** 图标 */
    private String icon;
    /** 颜色 */
    private String color;
    /** 排序 */
    @Max(value = 65535, message = "排序最大值为65535")
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /** 状态;1-启用,0-停用 */
    @NotNull(message = "状态不能为空")
    private Integer status;
    /** 备注信息 */
    @Size(max = 1024, message = "备注信息最大不能超过1024个字符")
    private String remark;
}
