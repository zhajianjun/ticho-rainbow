package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 邮箱验证码发送时图片验证码DTO
 *
 * @author zhajianjun
 * @date 2024-02-12 20:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "邮箱验证码发送时图片验证码DTO")
public class ImgCodeEmailDTO extends ImgCodeDTO {

    @ApiModelProperty(value = "邮箱", position = 20)
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

}
