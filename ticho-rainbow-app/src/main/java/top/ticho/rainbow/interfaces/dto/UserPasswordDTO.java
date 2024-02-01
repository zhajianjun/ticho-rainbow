package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户密码修改
 *
 * @author zhajianjun
 * @date 2021-05-29 9:25
 */
@Data
@EqualsAndHashCode()
@ApiModel("用户密码修改")
public class UserPasswordDTO {

    /** 主键标识 */
    @ApiModelProperty(value = "主键标识", position = 10)
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", position = 20)
    @NotBlank(message = "旧密码不能为空")
    private String password;

    /** 备注信息 */
    @ApiModelProperty(value = "新密码", position = 30)
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$", message = "密码必须包含字母、数字和特殊字符，且在6~16位之间")
    private String newPassword;

}
