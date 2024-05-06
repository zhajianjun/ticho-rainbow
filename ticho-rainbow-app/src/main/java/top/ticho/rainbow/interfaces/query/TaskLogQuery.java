package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 计划任务日志信息查询条件
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "计划任务日志信息查询条件")
public class TaskLogQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 任务ID */
    @ApiModelProperty(value = "任务ID", position = 20)
    private Long taskId;

    /** 任务内容 */
    @ApiModelProperty(value = "任务内容", position = 30)
    private String content;

    /** 任务参数 */
    @ApiModelProperty(value = "任务参数", position = 40)
    private String param;

    /** 执行时间 */
    @ApiModelProperty(value = "执行时间", position = 60)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] executeTime;

    /** 执行开始时间 */
    @ApiModelProperty(value = "执行开始时间", position = 70)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] startTime;

    /** 执行结束时间 */
    @ApiModelProperty(value = "执行结束时间", position = 80)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] endTime;

    /** 执行间隔开始 */
    @ApiModelProperty(value = "执行间隔开始", position = 90)
    private Integer consumeStart;

    /** 执行间隔结束 */
    @ApiModelProperty(value = "执行间隔结束", position = 92)
    private Integer consumeEnd;

    /** 链路id */
    @ApiModelProperty(value = "链路id", position = 110)
    private String traceId;

    /** 任务状态;1-执行成功,0-执行异常 */
    @ApiModelProperty(value = "任务状态;1-执行成功,0-执行异常", position = 120)
    private Integer status;

    /** 操作人 */
    @ApiModelProperty(value = "操作人", position = 130)
    private String operateBy;

    /** 是否异常 */
    @ApiModelProperty(value = "是否异常", position = 150)
    private Integer isErr;

    /** 异常信息 */
    @ApiModelProperty(value = "异常信息", position = 160)
    private String errMessage;

}
