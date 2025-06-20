package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 登录用户详细信息
 *
 * @author zhajianjun
 * @date 2025-06-01 00:15
 */
@Data
public class LoginUserDetailDTO {

    /** 主键编号 */
    private Long id;
    /** 账号;具有唯一性 */
    private String username;
    /** 昵称 */
    private String nickname;
    /** 真实姓名 */
    private String realname;
    /** 身份证号 */
    private String idcard;
    /** 性别;0-男,1-女 */
    private Integer sex;
    /** 年龄 */
    private Integer age;
    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    /** 家庭住址 */
    private String address;
    /** 学历 */
    private String education;
    /** 邮箱 */
    private String email;
    /** QQ号码 */
    private String qq;
    /** 微信号码 */
    private String wechat;
    /** 手机号码 */
    private String mobile;
    /** 头像地址 */
    private String photo;
    /** 最后登录ip地址 */
    private String lastIp;
    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTime;
    /** 用户状态;1-正常,2-未激活,3-已锁定,4-已注销 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
