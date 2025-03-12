package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 重置密码邮件发送
 *
 * @author zhajianjun
 * @date 2024-02-12 20:19
 */
@Data
public class ResetPassworEmailSendCommand {

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
