package top.ticho.rainbow.application.dto.command;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class UseSaveCommand {

    /** 账号;具有唯一性 */
    @NotBlank(message = "账号不能为空")
    @Length(max = 32, message = "账号长度不能超过{max}个字符")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
    /** 昵称 */
    @Length(max = 32, message = "昵称长度不能超过{max}个字符")
    private String nickname;
    /** 邮箱 */
    @Email(message = "邮箱格式不正确")
    private String email;
    /** 手机号码 */
    @Pattern(regexp = "^\\d{11}$", message = "手机号码格式不正确")
    private String mobile;
    /** 备注信息 */
    @Length(max = 256, message = "备注信息长度不能超过{max}个字符")
    private String remark;
    /** 角色编号列表 */
    @NotEmpty(message = "角色编号不能为空")
    private List<Long> roleIds;

}
