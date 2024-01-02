package top.ticho.intranet.server.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import top.ticho.boot.web.util.valid.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户信息DTO")
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @ApiModelProperty(value = "主键标识", position = 10)
    @NotNull(message = "编号不能为空", groups = ValidGroup.Upd.class)
    private Long id;

    /** 用户名 */
    @ApiModelProperty(value = "用户名", position = 20)
    @NotNull(message = "用户名不能为空", groups = ValidGroup.Add.class)
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码", position = 30)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "密码不能为空", groups = ValidGroup.Add.class)
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$", message = "密码必须包含字母、数字和特殊字符，且在6~16位之间")
    private String password;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 40)
    private String remark;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 60)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 70)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 80)
    private String updateBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间", position = 90)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
