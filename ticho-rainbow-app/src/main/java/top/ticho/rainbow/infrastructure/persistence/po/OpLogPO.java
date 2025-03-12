package top.ticho.rainbow.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 日志信息
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_op_log")
public class OpLogPO extends Model<OpLogPO> {

    /** 主键编号 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;    /** 名称 */
    private String name;    /** 请求地址 */
    private String url;    /** 请求类型 */
    private String type;    /** 请求方法 */
    private String position;    /** 请求体 */
    private String reqBody;    /** 请求参数 */
    private String reqParams;    /** 响应体 */
    private String resBody;    /** 请求开始时间 */
    private LocalDateTime startTime;    /** 请求结束时间 */
    private LocalDateTime endTime;    /** 请求间隔(毫秒) */
    private Integer consume;    /** mdc信息 */
    private String mdc;    /** 链路id */
    private String traceId;    /** 请求IP */
    private String ip;    /** 响应状态 */
    private Integer resStatus;    /** 操作人 */
    private String operateBy;    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;    /** 是否异常 */
    private Integer isErr;    /** 异常信息 */
    private String errMessage;
}