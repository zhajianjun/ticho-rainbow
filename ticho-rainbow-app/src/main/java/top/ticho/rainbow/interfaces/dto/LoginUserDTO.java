package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录用户信息
 *
 * @author zhajianjun
 * @date 2025-06-01 00:15
 */
@Data
public class LoginUserDTO {

    /** 账号;具有唯一性 */
    private String username;
    /** 昵称 */
    private String nickname;
    /** 真实姓名 */
    private String realname;
    /** 头像地址 */
    private String photo;
    /** 最后登录ip地址 */
    private String lastIp;
    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTime;

}
