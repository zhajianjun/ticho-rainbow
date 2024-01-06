package top.ticho.intranet.server.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 端口信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_port")
public class Port extends Model<Port> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键标识 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 客户端秘钥 */
    private String accessKey;

    /** 主机端口 */
    private Integer port;

    /** 客户端地址 */
    private String endpoint;

    /** 域名 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String domain;

    /** 是否开启;1-开启,0-关闭 */
    private Integer enabled;

    /** 是否永久;1-是,0-否 */
    private Integer forever;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;

    /** 协议类型 */
    private Integer type;

    /** 排序 */
    private Integer sort;

    /** 备注信息 */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /** 乐观锁;控制版本更改 */
    @Version
    private Long version;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 删除标识;0-未删除,1-已删除 */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;

}