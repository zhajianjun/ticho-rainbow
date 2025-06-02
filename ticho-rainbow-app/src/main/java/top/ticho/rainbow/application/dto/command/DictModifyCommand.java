package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 字典修改
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
public class DictModifyCommand {

    /** 编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 字典名称 */
    @NotBlank(message = "名称不能为空")
    private String name;
    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
