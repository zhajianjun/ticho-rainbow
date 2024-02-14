package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 图片验证码DTO
 *
 * @author zhajianjun
 * @date 2024-02-12 20:16:
 */
@Data
@ApiModel(value = "图片验证码DTO")
public class ImgCodeDTO {

    /** 验证码秘钥 */
    @NotBlank(message = "验证码秘钥不能为空", groups = ImgCodeValid.class)
    @ApiModelProperty(value = "验证码秘钥", position = 10000)
    private String imgKey;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空", groups = ImgCodeValid.class)
    @ApiModelProperty(value = "验证码", position = 100010)
    private String imgCode;

    public interface ImgCodeValid {
    }

}
