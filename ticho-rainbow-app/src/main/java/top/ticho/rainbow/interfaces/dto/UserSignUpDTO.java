package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(value = "用户名", example = "admin", position = 20)
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", position = 30)
    private String password;

}
