package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 用户密码修改
 *
 * @author zhajianjun
 * @date 2021-05-29 9:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("用户密码修改")
public class UserPasswordDTO extends PasswordDTO {

    /** 主键标识 */
    @ApiModelProperty(value = "用户名", position = 10)
    @NotBlank(message = "用户名不能为空")
    private String username;

}
