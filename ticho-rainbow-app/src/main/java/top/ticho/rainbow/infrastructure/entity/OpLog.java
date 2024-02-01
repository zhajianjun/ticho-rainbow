package top.ticho.rainbow.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_op_log")
public class OpLog extends Model<OpLog> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /** 请求地址 */
    private String url;

    /** 日志类型 */
    private String type;

    /** 请求方法 */
    private String method;

    /** 请求参数 */
    private String params;

    /** 内容 */
    private String message;

    /** 总耗时长（毫秒） */
    private Integer totalTime;

    /** 请求IP */
    private String ip;

    /** 操作人 */
    private String operateBy;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime operateTime;

    /** 是否异常 */
    private Integer isErr;

    /** 异常信息 */
    private String errMessage;

}