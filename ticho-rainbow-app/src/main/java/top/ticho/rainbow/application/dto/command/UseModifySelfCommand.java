package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class UseModifySelfCommand {

    /** 昵称 */
    private String nickname;
    /** 邮箱 */
    private String email;
    /** 手机号码 */
    private String mobile;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
