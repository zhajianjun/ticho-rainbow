package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 注册邮箱发送验证码
 *
 * @author zhajianjun
 * @date 2024-02-12 20:19
 */
@Data
public class SignUpEmailSendCommand {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    /** 验证码秘钥 */
    @NotBlank(message = "验证码秘钥不能为空")
    private String imgKey;
    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String imgCode;
}
