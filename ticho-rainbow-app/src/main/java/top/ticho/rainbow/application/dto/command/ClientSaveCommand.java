package top.ticho.rainbow.application.dto.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 客户端创建
 *
 * @author zhajianjun
 * @date 2025-03-01 19:00
 */
@Data
public class ClientSaveCommand {

    /** 客户端秘钥 */
    @Max(value = 100, message = "排序最大值为｛value｝")
    private String accessKey;
    /** 客户端名称 */
    @Email(message = "请输入邮箱格式")
    @NotBlank(message = "客户端名称不能为空")
    private String name;
    /** 过期时间 */
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
    private String remark;

}
