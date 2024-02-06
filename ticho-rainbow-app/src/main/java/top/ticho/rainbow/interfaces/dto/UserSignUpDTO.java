package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 用户注册DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@ApiModel(value = "用户注册DTO")
public class UserSignUpDTO {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", example = "admin", position = 10)
    private String username;

    @NotBlank(message = "用户名不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value = "邮箱", position = 20)
    private String email;

    @NotBlank(message = "邮箱验证码不能为空")
    @ApiModelProperty(value = "邮箱验证码", position = 30)
    private String emailCode;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", position = 40)
    private String password;

}
