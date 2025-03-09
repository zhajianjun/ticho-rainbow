package top.ticho.rainbow.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户角色菜单功能号详情
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data

@ApiModel(value = "用户角色菜单功能号详情")
public class UserRoleMenuDtlDTO {


    /** 主键编号 */
    private Long id;

    /** 账户;账户具有唯一性 */
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

    /** 角色id列表 */
    private List<Long> roleIds;

    /** 角色code列表 */
    private List<String> roleCodes;

    /** 菜单id列表 */
    private List<Long> menuIds;

    /** 权限标识 */
    private List<String> perms;

    /** 角色信息 */
    private List<RoleDTO> roles;

    /** 菜单信息 */
    private List<MenuDtlDTO> menus;

}
