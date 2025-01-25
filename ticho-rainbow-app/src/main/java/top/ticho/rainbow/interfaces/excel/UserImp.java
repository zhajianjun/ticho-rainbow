package top.ticho.rainbow.interfaces.excel;

import cn.hutool.core.lang.RegexPool;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.format.DateTimeFormat;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentFontStyle;
import cn.idev.excel.annotation.write.style.HeadFontStyle;
import cn.idev.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelBaseImp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息导出
 *
 * @author zhajianjun
 * @date 2024-05-09 18:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class UserImp extends ExcelBaseImp implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 账户;账户具有唯一性 */
    @ColumnWidth(20)
    @ExcelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 昵称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "昵称")
    private String nickname;

    /** 真实姓名 */
    @ColumnWidth(20)
    @ExcelProperty(value = "姓名")
    private String realname;

    /** 身份证号 */
    @ColumnWidth(20)
    @ExcelProperty(value = "身份证号")
    private String idcard;

    /** 性别;0-男,1-女 */
    @ColumnWidth(20)
    @ExcelProperty(value = "性别")
    private String sexName;

    /** 年龄 */
    @ColumnWidth(20)
    @ExcelProperty(value = "年龄")
    private Integer age;

    /** 出生日期 */
    @ColumnWidth(20)
    @ExcelProperty(value = "出生日期")
    @DateTimeFormat(value = "yyyy-MM-dd")
    private LocalDate birthday;

    /** 家庭住址 */
    @ColumnWidth(20)
    @ExcelProperty(value = "家庭住址")
    private String address;

    /** 学历 */
    @ColumnWidth(20)
    @ExcelProperty(value = "学历")
    private String education;

    /** 邮箱 */
    @ColumnWidth(20)
    @ExcelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    /** QQ号码 */
    @ColumnWidth(20)
    @ExcelProperty(value = "QQ号码")
    private String qq;

    /** 微信号码 */
    @ColumnWidth(20)
    @ExcelProperty(value = "微信号码")
    private String wechat;

    /** 手机号码 */
    @ColumnWidth(20)
    @ExcelProperty(value = "手机号码")
    @Pattern(regexp = RegexPool.MOBILE, message = "手机号码格式不正确")
    private String mobile;

}
