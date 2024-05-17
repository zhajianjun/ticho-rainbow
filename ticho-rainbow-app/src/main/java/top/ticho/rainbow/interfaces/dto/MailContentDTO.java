package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 邮件信息
 *
 * @author zhajianjun
 * @date 2024-05-17 16:34
 */
@Data
@ApiModel(value = "邮件信息")
public class MailContentDTO {

    @NotBlank(message = "收件人地址不能为空")
    @Email(message = "收件人地址格式错误")
    @ApiModelProperty(value = "收件人地址", position = 20)
    private String to;

    @NotBlank(message = "邮件主题不能为空")
    @ApiModelProperty(value = "邮件主题", position = 30)
    private String subject;

    @NotBlank(message = "邮件内容不能为空")
    @ApiModelProperty(value = "邮件内容", position = 40)
    private String content;

    @ApiModelProperty(value = "抄送地址", position = 50)
    private List<String> cc;

}
