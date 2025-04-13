package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户角色信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class UserBindRoleCommand {


    /** 用户id */
    @NotNull(message = "用户id不能为空")
    private Long userId;
    /** 角色id列表 */
    @NotEmpty(message = "角色id列表不能为空")
    private List<Long> roleIds;

}
