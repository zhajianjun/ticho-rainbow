package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.web.util.valid.TiValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 计划任务信息DTO
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "计划任务信息DTO")
public class TaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @NotNull(message = "任务ID不能为空", groups = {TiValidGroup.Upd.class})
    @ApiModelProperty(value = "任务ID", position = 10)
    private Long id;

    /** 任务名称 */
    @ApiModelProperty(value = "任务名称", position = 20)
    @NotBlank(message = "任务名称不能为空")
    private String name;

    /** 任务内容 */
    @ApiModelProperty(value = "任务内容", position = 30)
    @NotBlank(message = "任务内容不能为空")
    private String content;

    /** 执行参数 */
    @ApiModelProperty(value = "任务参数", position = 40)
    private String param;

    /** cron执行表达式 */
    @ApiModelProperty(value = "cron执行表达式", position = 50)
    @NotBlank(message = "cron执行表达式不能为空")
    private String cronExpression;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 60)
    private String remark;

    /** 任务状态;1-正常,0-停用 */
    @ApiModelProperty(value = "任务状态;1-正常,0-停用", position = 70)
    private Integer status;

    /** 乐观锁;控制版本更改 */
    @ApiModelProperty(value = "乐观锁;控制版本更改", position = 80)
    private Long version;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 90)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 100)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人", position = 110)
    private String updateBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 120)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /** 删除标识;0-未删除,1-已删除 */
    @ApiModelProperty(value = "删除标识;0-未删除,1-已删除", position = 130)
    private Integer isDelete;

}
