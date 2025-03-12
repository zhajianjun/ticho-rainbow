package top.ticho.rainbow.application.task;

import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.handle.SseHandle;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.trace.common.prop.TraceProperty;

/**
 * sse心跳
 *
 * @author zhajianjun
 * @date 2024-05-23 10:44
 */
@Component
public class SseHeatBeatTask extends AbstracTask<String> {

    private final SseHandle sseHandle;
    public SseHeatBeatTask(Environment environment, TraceProperty traceProperty, TaskLogRepository taskLogRepository, SseHandle sseHandle) {
        super(environment, traceProperty, taskLogRepository);
        this.sseHandle = sseHandle;
    }

    @Override
    public void run(JobExecutionContext context) {
        sseHandle.heatbeat();
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
