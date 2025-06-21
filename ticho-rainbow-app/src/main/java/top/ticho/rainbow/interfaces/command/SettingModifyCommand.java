package top.ticho.rainbow.interfaces.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 配置信息保存
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Data
public class SettingModifyCommand {

    /** 主键编号 */
    @NotNull(message = "主键编号不能为空")
    private Long id;
    /** value */
    @NotBlank(message = "value不能为空")
    @Size(max = 100, message = "value最大不能超过｛max｝个字符")
    private String value;
    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /** 备注信息 */
    @Size(max = 256, message = "备注信息最大不能超过｛max｝个字符")
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}