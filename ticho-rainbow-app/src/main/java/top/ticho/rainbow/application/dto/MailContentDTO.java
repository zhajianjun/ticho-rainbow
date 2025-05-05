package top.ticho.rainbow.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 邮件信息
 *
 * @author zhajianjun
 * @date 2024-05-17 16:34
 */
@Data
public class MailContentDTO {

    @NotBlank(message = "收件人地址不能为空")
    @Email(message = "收件人地址格式错误")
    private String to;
    @NotBlank(message = "邮件主题不能为空")
    private String subject;
    @NotBlank(message = "邮件内容不能为空")
    private String content;
    private List<String> cc;

}
