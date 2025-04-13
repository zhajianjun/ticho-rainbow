package top.ticho.rainbow.application.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 计划任务信息查询条件
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 任务ID */
    private Long id;
    /** 任务名称 */
    private String name;
    /** 任务内容 */
    private String content;
    /** 执行参数 */
    private String param;
    /** cron执行表达式 */
    private String cronExpression;
    /** 备注信息 */
    private String remark;
    /** 任务状态;1-启用,0-停用 */
    private Integer status;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
