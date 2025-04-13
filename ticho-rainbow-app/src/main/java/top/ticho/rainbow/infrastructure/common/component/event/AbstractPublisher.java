package top.ticho.rainbow.infrastructure.common.component.event;

import cn.hutool.core.thread.ThreadUtil;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 抽象发布者类
 *
 * @author zhajianjun
 * @date 2024-05-06 09:39
 */
public abstract class AbstractPublisher<M> {

    /** Disruptor */
    private final Disruptor<ContextEvent<M>> disruptor;
    /** 环形存储区 */
    private RingBuffer<ContextEvent<M>> ringBuffer;

    /**
     * @param ringBufferSize 环形缓冲区大小，必须是2的幂次方
     */
    public AbstractPublisher(int ringBufferSize) {
        this(ringBufferSize, new TimeoutBlockingWaitStrategy(30, TimeUnit.SECONDS));
    }

    /**
     * @param ringBufferSize 环形缓冲区大小，必须是2的幂次方
     * @param waitStrategy   等待策略
     */
    public AbstractPublisher(int ringBufferSize, WaitStrategy waitStrategy) {
        // 线程工厂
        ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory("ticho-event-", false);
        // 生产者类型，多个
        ProducerType producerType = ProducerType.MULTI;
        // 事件工厂
        EventFactory<ContextEvent<M>> eventFactory = ContextEvent::new;
        this.disruptor = new Disruptor<>(eventFactory, ringBufferSize, threadFactory, producerType, waitStrategy);
        registerListen(disruptor);
    }

    public abstract void registerListen(Disruptor<ContextEvent<M>> disruptor);

    /**
     * 事件生产
     */
    public void product(String name, M data) {
        // 获取下一个序号
        long ringSeq = ringBuffer.next();
        try {
            // 根据序号创建数据
            ContextEvent<M> contextEvent = ringBuffer.get(ringSeq);
            contextEvent.setName(name);
            contextEvent.setData(data);
        } finally {
            // 事件发布
            ringBuffer.publish(ringSeq);
        }
    }

    public void start() {
        // 启动Disruptor
        ringBuffer = disruptor.start();
    }

    public void stop() {
        disruptor.shutdown();
    }

}
