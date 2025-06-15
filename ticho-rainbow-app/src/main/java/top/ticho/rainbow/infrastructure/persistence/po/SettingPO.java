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
 * 配置信息PO
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_setting")
public class SettingPO extends Model<SettingPO> {

    /** 主键编号 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /** key */
    @TableField("`key`")
    private String key;
    /** value */
    @TableField("`value`")
    private String value;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @Version
    @TableField(fill = FieldFill.INSERT)
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
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDelete;

}