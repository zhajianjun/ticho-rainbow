package top.ticho.rainbow.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class UseSaveCommand {

    /** 账户;账户具有唯一性 */
    @NotBlank(message = "账户不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
    /** 昵称 */
    private String nickname;
    /** 邮箱 */
    private String email;
    /** 手机号码 */
    private String mobile;
    /** 备注信息 */
    private String remark;
    /** 角色编号列表 */
    @NotEmpty(message = "角色编号不能为空")
    private List<Long> roleIds;

}
