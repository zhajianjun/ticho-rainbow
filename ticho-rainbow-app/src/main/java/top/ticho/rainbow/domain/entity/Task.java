package top.ticho.rainbow.domain.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.TaskModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.view.util.TiAssert;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 计划任务信息
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Getter
@Builder
public class Task implements Entity {

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
    /** 任务状态;1-启用,0-禁用 */
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

    public void modify(TaskModifyVO taskModifyVo) {
        if (isEnable()) {
            TiAssert.isTrue(Objects.equals(content, taskModifyVo.content()), StrUtil.format("任务已启用，任务内容不能修改，任务：{}", name));
            TiAssert.isTrue(Objects.equals(param, taskModifyVo.param()), StrUtil.format("任务已启用，任务参数不能修改，任务：{}", name));
            TiAssert.isTrue(Objects.equals(cronExpression, taskModifyVo.cronExpression()), StrUtil.format("任务已启用，Cron执行表达式不能修改，任务：{}", name));
        }
        this.name = taskModifyVo.name();
        this.content = taskModifyVo.content();
        this.param = taskModifyVo.param();
        this.cronExpression = taskModifyVo.cronExpression();
        this.remark = taskModifyVo.remark();
        this.version = taskModifyVo.version();
    }

    public boolean isEnable() {
        return Objects.equals(CommonStatus.ENABLE.code(), status);
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()),
            StrUtil.format("只有[{}]状态才能执行启用操作，任务：{}", disable.message(), name));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()),
            StrUtil.format("只有[{}]状态才能执行禁用操作，任务：{}", enable.message(), name));
        this.status = CommonStatus.DISABLE.code();
    }

}