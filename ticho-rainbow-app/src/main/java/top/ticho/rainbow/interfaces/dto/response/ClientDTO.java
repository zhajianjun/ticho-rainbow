package top.ticho.rainbow.interfaces.dto.response;

import cn.idev.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户端信息DTO
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
public class ClientDTO {


    /** 主键标识 */
    private Long id;
    /** 客户端秘钥 */
    private String accessKey;
    /** 客户端名称 */
    private String name;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    /** 连接时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime connectTime;
    /** 通道状态;1-激活,0-未激活 */
    private Integer channelStatus = 0;

}
