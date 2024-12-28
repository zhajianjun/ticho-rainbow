package top.ticho.rainbow.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 计划任务日志信息
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_task_log")
public class TaskLog extends Model<TaskLog> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 任务ID */
    private Long taskId;

    /** 任务内容 */
    private String content;

    /** 任务参数 */
    private String param;

    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime executeTime;

    /** 执行开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /** 执行结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    /** 执行间隔(毫秒) */
    private Integer consume;

    /** mdc信息 */
    private String mdc;

    /** 链路id */
    private String traceId;

    /** 执行状态;1-执行成功,0-执行异常 */
    private Integer status;

    /** 操作人 */
    private String operateBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 是否异常 */
    private Integer isErr;

    /** 异常信息 */
    private String errMessage;

}