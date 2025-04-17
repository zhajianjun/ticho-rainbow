package top.ticho.rainbow.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class PortPO extends Model<PortPO> {

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
    private String domain;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 过期时间 */
    private LocalDateTime expireAt;
    /** 协议类型 */
    private Integer type;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @Version
    private Long version;
    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 修改人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /** 删除标识;0-未删除,1-已删除 */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;
}