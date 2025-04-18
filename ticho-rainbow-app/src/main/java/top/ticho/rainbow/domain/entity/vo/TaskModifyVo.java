package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

/**
 * @param name           任务名称
 * @param content        任务内容
 * @param param          执行参数
 * @param cronExpression cron执行表达式
 * @param remark         备注信息
 * @param status         任务状态;1-启用,0-停用
 * @param version        版本号
 * @author zhajianjun
 * @date 2025-03-09 16:06
 */
public record TaskModifyVo(String name, String content, String param, String cronExpression, String remark, Integer status, Long version) {

}
