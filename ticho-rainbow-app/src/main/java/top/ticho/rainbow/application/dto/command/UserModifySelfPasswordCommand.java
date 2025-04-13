package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 密码DTO
 *
 * @author zhajianjun
 * @date 2024-03-24 14:39
 */
@Data
public class UserModifySelfPasswordCommand {

    @NotBlank(message = "旧密码不能为空")
    private String password;
    /** 备注信息 */
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$", message = "密码必须包含字母、数字和特殊字符，且在6~16位之间")
    private String newPassword;

}
