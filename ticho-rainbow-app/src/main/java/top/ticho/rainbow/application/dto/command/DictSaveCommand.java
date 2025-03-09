package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 字典创建命令
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
public class DictSaveCommand {

    /** 字典编码 */
    @NotBlank(message = "编码不能为空")
    private String code;
    /** 字典名称 */
    @NotBlank(message = "名称不能为空")
    private String name;
    /** 是否系统字典;1-是,0-否 */
    @NotNull(message = "是否系统字典不能为空")
    private Integer isSys;
    /** 状态;1-启用,0-停用 */
    @NotNull(message = "字典状态不能为空")
    private Integer status;
    /** 备注信息 */
    private String remark;

}
