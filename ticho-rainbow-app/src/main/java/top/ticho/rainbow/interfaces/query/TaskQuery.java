package top.ticho.rainbow.interfaces.query;

import java.io.Serializable;
import top.ticho.boot.view.core.BasePageQuery;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 定时任务调度信息查询条件
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "定时任务调度信息查询条件")
public class TaskQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @ApiModelProperty(value = "任务ID", position = 10)
    private Long id;

    /** 任务名称 */
    @ApiModelProperty(value = "任务名称", position = 20)
    private String name;

    /** 任务内容 */
    @ApiModelProperty(value = "任务内容", position = 30)
    private String content;

    /** 执行参数 */
    @ApiModelProperty(value = "任务参数", position = 40)
    private String param;

    /** cron执行表达式 */
    @ApiModelProperty(value = "cron执行表达式", position = 50)
    private String cronExpression;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 60)
    private String remark;

    /** 任务状态;1-正常,0-停用 */
    @ApiModelProperty(value = "任务状态;1-正常,0-停用", position = 70)
    private Integer status;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 90)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 100)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
