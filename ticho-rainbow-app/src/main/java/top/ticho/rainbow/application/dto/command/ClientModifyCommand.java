package top.ticho.rainbow.application.dto.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户端修改
 *
 * @author zhajianjun
 * @date 2025-03-01 19:00
 */
@Data
public class ClientModifyCommand {

    /** 编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 客户端名称 */
    @Email(message = "请输入邮箱格式")
    @NotBlank(message = "客户端名称不能为空")
    private String name;
    /** 过期时间 */
    @NotNull(message = "过期时间不能为空")
    @Future(message = "过期时间必须大于当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;
    /** 状态;1-启用,0-停用 */
    @NotNull(message = "状态不能为空")
    private Integer status;
    /** 排序 */
    @Max(value = 65535, message = "排序最大值为{value}")
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /** 备注信息 */
    @Size(max = 1024, message = "备注信息最大不能超过{max}个字符")
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
