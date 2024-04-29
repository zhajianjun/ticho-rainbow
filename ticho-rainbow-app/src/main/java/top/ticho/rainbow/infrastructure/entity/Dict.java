package top.ticho.rainbow.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_dict")
public class Dict extends Model<Dict> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 字典编码 */
    private String code;

    /** 字典名称 */
    private String name;

    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;

    /** 状态;1-启用,0-停用 */
    private Integer status;

    /** 备注信息 */
    private String remark;

    /** 乐观锁;控制版本更改 */
    private Long version;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 删除标识;0-未删除,1-已删除 */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;

}