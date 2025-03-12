package top.ticho.rainbow.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 端口信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Getter
@Builder
public class Port {

    /** 主键标识 */
    private Long id;    /** 客户端秘钥 */
    private String accessKey;    /** 主机端口 */
    private Integer port;    /** 客户端地址 */
    private String endpoint;    /** 域名 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String domain;    /** 状态;1-启用,0-停用 */
    private Integer status;    /** 过期时间 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime expireAt;    /** 协议类型 */
    private Integer type;    /** 排序 */
    private Integer sort;    /** 备注信息 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;    /** 版本号 */
    @Version
    private Long version;    /** 创建人 */
    private String createBy;    /** 创建时间 */
    private LocalDateTime createTime;    /** 修改人 */
    private String updateBy;    /** 修改时间 */
    private LocalDateTime updateTime;
}