package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色菜单关联关系
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class RoleBindMenuCommand {

    /** 角色id */
    @NotNull(message = "角色id不能为空")
    private Long roleId;
    /** 菜单id列表 */
    private List<Long> menuIds;

}