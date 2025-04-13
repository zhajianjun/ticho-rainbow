package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

import jakarta.validation.constraints.NotNull;

/**
 * @author zhajianjun
 * @date 2025-03-23 16:29
 */
@Value
public class UserModifyVO {

    /** 昵称 */
    String nickname;
    /** 邮箱 */
    String email;
    /** 手机号码 */
    String mobile;
    /** 备注信息 */
    String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    Long version;

}
