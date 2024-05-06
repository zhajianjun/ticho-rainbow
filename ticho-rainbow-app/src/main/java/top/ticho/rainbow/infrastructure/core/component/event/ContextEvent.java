package top.ticho.rainbow.infrastructure.core.component.event;

import lombok.Data;

/**
 * 上下文事件数据
 *
 * @author zhajianjun
 * @date 2024-05-06 09:36
 */
@Data
public class ContextEvent<M> {

    /** 事件名称 */
    private String name;

    /** 事件数据 */
    private M data;

}
