package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.TaskModifyVo;

import java.time.LocalDateTime;

/**
 * 计划任务信息
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Getter
@Builder
public class Task {

    /** 任务ID */
    private Long id;
    /** 任务名称 */
    private String name;
    /** 任务内容 */
    private String content;
    /** 任务参数 */
    private String param;
    /** cron执行表达式 */
    private String cronExpression;
    /** 备注信息 */
    private String remark;
    /** 任务状态;1-启用,0-停用 */
    private Integer status;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;

    public void modify(TaskModifyVo taskModifyVo) {
        this.name = taskModifyVo.name();
        this.content = taskModifyVo.content();
        this.param = taskModifyVo.param();
        this.cronExpression = taskModifyVo.cronExpression();
        this.remark = taskModifyVo.remark();
        this.status = taskModifyVo.status();
        this.version = taskModifyVo.version();
    }

}