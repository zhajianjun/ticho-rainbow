package top.ticho.rainbow.interfaces.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 用户密码修改
 *
 * @author zhajianjun
 * @date 2021-05-29 9:25
 */
@Data
public class UserModifyPasswordCommand {

    /** 主键标识 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 旧密码 */
    @NotBlank(message = "旧密码不能为空")
    private String password;
    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$", message = "密码必须包含字母、数字和特殊字符，且在6~16位之间")
    private String newPassword;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
