package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 计划任务信息DTO
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
public class TaskModifyCommand {

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
    /** 任务状态;1-正常,0-停用 */
    private Integer status;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;
}
