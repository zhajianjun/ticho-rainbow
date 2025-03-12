package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhajianjun
 * @date 2025-03-08 14:44
 */
@Data
public class RoleStatusModifyCommand {

    /** 状态;1-正常,0-禁用 */
    @NotNull(message = "角色状态不能为空")
    private Integer status;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;
}
