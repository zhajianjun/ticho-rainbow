package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息查询条件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户信息查询条件")
public class UserQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号列表", position = 9)
    private List<Long> ids;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 账户;账户具有唯一性 */
    @ApiModelProperty(value = "账户;账户具有唯一性", position = 20)
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
    private String qq;

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

}
