package top.ticho.rainbow.interfaces.dto.command;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 重置用户密码
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class ResetPasswordCommand {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$", message = "密码必须包含字母、数字和特殊字符，且在6~16位之间")
    private String password;
    /** 邮箱 */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    /** 邮箱验证码 */
    @NotBlank(message = "邮箱验证码不能为空")
    private String emailCode;

}
