package top.ticho.rainbow.domain.entity.vo;

/**
 * @param nickname 昵称
 * @param realname 姓名
 * @param email    邮箱
 * @param mobile   手机号码
 * @param remark   备注信息
 * @param version  版本号
 * @author zhajianjun
 * @date 2025-03-23 16:29
 */
public record UserModifyVO(
    String nickname,
    String realname,
    String email,
    String mobile,
    String remark,
    Long version
) {

}
