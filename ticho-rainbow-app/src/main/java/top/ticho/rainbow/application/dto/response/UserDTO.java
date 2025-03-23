package top.ticho.rainbow.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import top.ticho.starter.web.util.valid.TiValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class UserDTO {

    /** 主键编号 */
    @NotNull(message = "主键编号不能为空", groups = TiValidGroup.Upd.class)
    private Long id;
    /** 账户;账户具有唯一性 */
    @NotBlank(message = "账户不能为空")
    private String username;
    /** 密码 */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "密码不能为空", groups = TiValidGroup.Add.class)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastTime;
    /** 用户状态;1-正常,2-未激活,3-已锁定,4-已注销 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 角色编号列表 */
    private List<Long> roleIds;
    /** 角色信息 */
    private List<RoleDTO> roles;

}
