package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 计划任务日志信息
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Getter
@Builder
public class TaskLog {

    /** 主键编号 */
    private Long id;    /** 任务ID */
    private Long taskId;    /** 任务内容 */
    private String content;    /** 任务参数 */
    private String param;    /** 执行时间 */
    private LocalDateTime executeTime;    /** 执行开始时间 */
    private LocalDateTime startTime;    /** 执行结束时间 */
    private LocalDateTime endTime;    /** 执行间隔(毫秒) */
    private Integer consume;    /** mdc信息 */
    private String mdc;    /** 链路id */
    private String traceId;    /** 执行状态;1-执行成功,0-执行异常 */
    private Integer status;    /** 操作人 */
    private String operateBy;    /** 创建时间 */
    private LocalDateTime createTime;    /** 是否异常 */
    private Integer isErr;    /** 异常信息 */
    private String errMessage;}