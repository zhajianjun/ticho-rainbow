package top.ticho.rainbow.interfaces.dto.response;

import lombok.Data;
import top.ticho.starter.security.dto.LoginRequest;

/**
 * 登录DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class LoginDTO implements LoginRequest {

    /** 验证码秘钥 */
    private String imgKey;
    /** 验证码 */
    private String imgCode;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;

}
