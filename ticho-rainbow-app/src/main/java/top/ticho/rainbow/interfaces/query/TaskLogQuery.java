package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 计划任务日志查询条件
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskLogQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 主键编号 */
    private Long id;
    /** 任务ID */
    private Long taskId;
    /** 任务内容 */
    private String content;
    /** 任务参数 */
    private String param;
    /** 执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] executeTime;
    /** 执行开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] startTime;
    /** 执行结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] endTime;
    /** 执行间隔开始 */
    private Integer consumeStart;
    /** 执行间隔结束 */
    private Integer consumeEnd;
    /** 链路id */
    private String traceId;
    /** 执行状态;1-执行成功,0-执行异常 */
    private Integer status;
    /** 操作人 */
    private String operateBy;
    /** 是否异常 */
    private Integer isErr;
    /** 异常信息 */
    private String errMessage;

}
