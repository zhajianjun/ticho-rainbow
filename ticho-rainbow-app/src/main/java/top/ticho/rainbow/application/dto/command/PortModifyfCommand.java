package top.ticho.rainbow.application.dto.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 端口信息修改
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
public class PortModifyfCommand {

    /** 编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
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
    private String domain;
    /** 状态;1-启用,0-停用 */
    @NotNull(message = "是否开启不能为空")
    private Integer status;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "过期时间不能为空")
    private LocalDateTime expireAt;
    /** 协议类型 */
    @NotNull(message = "协议类型不能为空")
    private Integer type;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
