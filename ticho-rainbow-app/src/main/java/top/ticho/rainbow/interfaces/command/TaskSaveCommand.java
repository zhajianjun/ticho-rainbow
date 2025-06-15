package top.ticho.rainbow.interfaces.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 计划任务创建
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
public class TaskSaveCommand {

    /** 任务名称 */
    @NotBlank(message = "任务名称不能为空")
    private String name;
    /** 任务内容 */
    @NotBlank(message = "任务内容不能为空")
    private String content;
    /** 执行参数 */
    private String param;
    /** cron执行表达式 */
    @NotBlank(message = "cron执行表达式不能为空")
    private String cronExpression;
    /** 备注信息 */
    private String remark;

}
