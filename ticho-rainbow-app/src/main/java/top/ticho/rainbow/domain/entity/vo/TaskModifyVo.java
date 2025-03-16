package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

/**
 * @author zhajianjun
 * @date 2025-03-09 16:06
 */
@Value
public class TaskModifyVo {

    /** 任务名称 */
    String name;
    /** 任务内容 */
    String content;
    /** 执行参数 */
    String param;
    /** cron执行表达式 */
    String cronExpression;
    /** 备注信息 */
    String remark;
    /** 任务状态;1-正常,0-停用 */
    Integer status;
    /** 版本号 */
    Long version;

}
