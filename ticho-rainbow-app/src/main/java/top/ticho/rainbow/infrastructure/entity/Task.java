package top.ticho.rainbow.infrastructure.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

/**
 * 定时任务调度信息
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_task")
public class Task extends Model<Task> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务名称 */
    private String name;

    /** 任务内容 */
    private String content;

    /** 任务参数 */
    private String param;

    /** cron执行表达式 */
    private String cronExpression;

    /** 备注信息 */
    private String remark;

    /** 任务状态;1-正常,0-停用 */
    private Integer status;

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