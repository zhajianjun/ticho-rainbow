package top.ticho.rainbow.interfaces.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class LoginUserModifyCommand {

    /** 昵称 */
    @Length(max = 32, message = "昵称长度不能超过{max}个字符")
    private String nickname;
    /** 姓名 */
    @Length(max = 32, message = "姓名长度不能超过{max}个字符")
    private String realname;
    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    private String email;
    /** 手机号码 */
    @Pattern(regexp = "^\\d{11}$", message = "手机号码格式不正确")
    private String mobile;
    /** 备注信息 */
    @Length(max = 256, message = "备注信息长度不能超过{max}个字符")
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
