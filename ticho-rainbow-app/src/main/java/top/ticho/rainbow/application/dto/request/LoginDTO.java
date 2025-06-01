package top.ticho.rainbow.application.dto.request;

import lombok.Data;
import top.ticho.starter.security.dto.LoginRequest;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class LoginDTO implements LoginRequest {

    /** 验证码秘钥 */
    @NotBlank(message = "验证码秘钥不能为空")
    private String imgKey;
    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String imgCode;
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

}
