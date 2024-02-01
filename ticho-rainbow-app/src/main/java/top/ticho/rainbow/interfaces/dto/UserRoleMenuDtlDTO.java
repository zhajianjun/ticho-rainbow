package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户角色菜单功能号详情")
public class UserRoleMenuDtlDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @ApiModelProperty(value = "用户id", position = 10)
    private Long id;

    /** 账户;账户具有唯一性 */
    @ApiModelProperty(value = "账户", position = 20)
    private String username;

    /** 昵称 */
    @ApiModelProperty(value = "昵称", position = 40)
    private String nickname;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名", position = 50)
    private String realname;

    /** 身份证号 */
    @ApiModelProperty(value = "身份证号", position = 60)
    private String idcard;

    /** 性别;0-男,1-女 */
    @ApiModelProperty(value = "性别;0-男,1-女", position = 70)
    private Integer sex;

    /** 年龄 */
    @ApiModelProperty(value = "年龄", position = 80)
    private Integer age;

    /** 出生日期 */
    @ApiModelProperty(value = "出生日期", position = 90)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;

    /** 家庭住址 */
    @ApiModelProperty(value = "家庭住址", position = 100)
    private String address;

    /** 学历 */
    @ApiModelProperty(value = "学历", position = 110)
    private String education;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱", position = 120)
    private String email;

    /** QQ号码 */
    @ApiModelProperty(value = "QQ号码", position = 130)
    private Long qq;

    /** 微信号码 */
    @ApiModelProperty(value = "微信号码", position = 140)
    private String wechat;

    /** 手机号码 */
    @ApiModelProperty(value = "手机号码", position = 150)
    private String mobile;

    /** 头像地址 */
    @ApiModelProperty(value = "头像地址", position = 160)
    private String photo;

    /** 最后登录ip地址 */
    @ApiModelProperty(value = "最后登录ip地址", position = 170)
    private String lastIp;

    /** 最后登录时间 */
    @ApiModelProperty(value = "最后登录时间", position = 180)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime lastTime;

    /** 用户状态;1-正常,2-未激活,3-已锁定,4-已注销 */
    @ApiModelProperty(value = "用户状态;1-正常,2-未激活,3-已锁定,4-已注销", position = 190)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 210)
    private String remark;

    /** 角色id列表 */
    @ApiModelProperty(value = "角色id列表", position = 220)
    private List<Long> roleIds;

    /** 角色code列表 */
    @ApiModelProperty(value = "角色code列表", position = 230)
    private List<String> roleCodes;

    /** 菜单id列表 */
    @ApiModelProperty(value = "菜单id列表", position = 240)
    private List<Long> menuIds;

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", position = 250)
    private List<String> perms;

    /** 角色信息 */
    @ApiModelProperty(value = "角色信息", position = 260)
    private List<RoleDTO> roles;

    /** 菜单信息 */
    @ApiModelProperty(value = "菜单信息", position = 270)
    private List<MenuDtlDTO> menus;

}
