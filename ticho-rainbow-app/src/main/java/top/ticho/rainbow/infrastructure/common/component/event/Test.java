package top.ticho.rainbow.infrastructure.common.component.event;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhajianjun
 * @date 2024-05-06 10:09
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        AbstractListener<String> listener = getListener(atomicInteger);
        AbstractPublisher<String> publisher = getPublisher(listener);
        publisher.start();
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                ThreadUtil.safeSleep(1000 * 2);
                publisher.product("", finalI + 1 + "");
            });
            thread.start();
        }
        ThreadUtil.safeSleep(1000 * 5);
        System.out.println(atomicInteger);
        publisher.stop();
    }

    private static AbstractPublisher<String> getPublisher(AbstractListener<String> abstractListener) {
        return new AbstractPublisher<String>(1024 * 2) {
            @Override
            public void registerListen(Disruptor<ContextEvent<String>> disruptor) {
                EventHandlerGroup<ContextEvent<String>> handlerGroup = disruptor.handleEventsWithWorkerPool(abstractListener, abstractListener);
                handlerGroup.then(abstractListener);
            }
        };
    }

    private static AbstractListener<String> getListener(AtomicInteger atomicInteger) {
        return new AbstractListener<String>() {
            @Override
            public void consume(String data, Long sequence, Boolean endOfBatch) {
                String format = StrUtil.format("data:{} sequence:{} endOfBatch:{}", data, sequence, endOfBatch);
                atomicInteger.incrementAndGet();
                log.info(format);
            }
        };
    }

}
