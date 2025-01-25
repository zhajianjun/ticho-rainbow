package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 计划任务信息查询条件
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "计划任务信息查询条件")
public class TaskQuery extends TiPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号列表 */
    @ApiModelProperty(value = "主键编号列表", position = 9)
    private List<Long> ids;

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
