package top.ticho.rainbow.domain.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * @author zhajianjun
 * @date 2025-03-09 16:06
 */
@Getter
@Builder
public class TaskModifyVo {
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
    /** 任务状态;1-正常,0-停用 */
    private Integer status;
    /** 版本号 */
    private Long version;
}
