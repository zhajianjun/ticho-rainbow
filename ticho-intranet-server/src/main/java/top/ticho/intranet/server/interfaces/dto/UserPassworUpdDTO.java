package top.ticho.intranet.server.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2023-10-01 09:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户密码修改DTO")
public class UserPassworUpdDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(value = "主键标识", position = 10)
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", position = 20)
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /** 备注信息 */
    @ApiModelProperty(value = "新密码空", position = 30)
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$", message = "密码必须包含字母、数字和特殊字符，且在6~16位之间")
    private String password;

}
