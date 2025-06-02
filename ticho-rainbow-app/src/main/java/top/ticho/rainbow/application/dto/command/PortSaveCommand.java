package top.ticho.rainbow.application.dto.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 端口信息创建
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
public class PortSaveCommand {

    /** 客户端秘钥 */
    @NotBlank(message = "客户端秘钥不能为空")
    private String accessKey;
    /** 主机端口 */
    @NotNull(message = "主机端口不能为空")
    @Max(value = 65535, message = "主机端口不大于{value}")
    @Min(value = 1, message = "主机端口不小于{value}")
    private Integer port;
    /** 客户端地址 */
    @NotBlank(message = "客户端地址不能为空")
    @Pattern(message = "客户端地址格式不正确[ip:port]", regexp = "\\b(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-5][0-5][0-3][0-5])\\b")
    private String endpoint;
    /** 域名 */
    @Pattern(message = "域名格式不正确", regexp = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$")
    @Size(max = 100, message = "域名最大不能超过{max}个字符")
    private String domain;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "过期时间不能为空")
    @Future(message = "过期时间必须大于当前时间")
    private LocalDateTime expireAt;
    /** 协议类型 */
    @NotNull(message = "协议类型不能为空")
    private Integer type;
    /** 排序 */
    @Max(value = 65535, message = "排序最大值为{value}")
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /** 备注信息 */
    @Size(max = 1024, message = "备注信息最大不能超过{max}个字符")
    private String remark;

}
