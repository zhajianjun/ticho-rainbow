package top.ticho.rainbow.interfaces.command;

import lombok.Data;
import top.ticho.starter.security.dto.LoginRequest;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录
 *
 * @author zhajianjun
 * @date 2025-06-08 14:17
 */
@Data
public class LoginCommand implements LoginRequest {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
    /** 验证码秘钥 */
    private String imgKey;
    /** 验证码 */
    private String imgCode;

}
