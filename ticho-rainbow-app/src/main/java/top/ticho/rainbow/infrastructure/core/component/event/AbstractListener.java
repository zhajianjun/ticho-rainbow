package top.ticho.rainbow.infrastructure.core.component.event;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 抽象订阅者类
 *
 * @author zhajianjun
 * @date 2024-05-06 09:36
 */
public abstract class AbstractListener<M> implements EventHandler<ContextEvent<M>>, WorkHandler<ContextEvent<M>> {

    /**
     * 事件消费
     *
     * @param contextEvent      事件
     * @param sequence   序列
     * @param endOfBatch 是否达到批次大小或者到达批次末尾
     */
    @Override
    public void onEvent(ContextEvent<M> contextEvent, long sequence, boolean endOfBatch) {
        consume(contextEvent.getData(), sequence, endOfBatch);
    }

    @Override
    public void onEvent(ContextEvent<M> contextEvent) {
        consume(contextEvent.getData(), null, null);
    }

    /**
     * 事件消费
     */
    protected abstract void consume(M data, Long sequence, Boolean endOfBatch);

}
