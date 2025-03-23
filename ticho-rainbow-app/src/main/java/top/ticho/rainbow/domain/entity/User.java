package top.ticho.rainbow.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.application.dto.UserHelper;
import top.ticho.rainbow.domain.entity.vo.UserModifyVO;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Getter
@Builder
public class User implements UserHelper {

    /** 主键编号 */
    private Long id;
    /** 账户;账户具有唯一性 */
    private String username;
    /** 密码 */
    private String password;
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
    private LocalDateTime lastTime;
    /** 用户状态;1-正常,2-未激活,3-已锁定,4-已注销 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;

    public void modify(UserModifyVO modifyVo) {
        this.nickname = modifyVo.getNickname();
        this.email = modifyVo.getEmail();
        this.mobile = modifyVo.getMobile();
        this.remark = modifyVo.getRemark();
        this.version = modifyVo.getVersion();
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyPhoto(String photo) {
        this.photo = photo;
    }

}