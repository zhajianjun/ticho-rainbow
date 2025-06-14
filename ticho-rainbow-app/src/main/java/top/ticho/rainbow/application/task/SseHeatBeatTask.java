package top.ticho.rainbow.application.task;

import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.service.SseHandle;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.trace.common.prop.TiTraceProperty;

/**
 * sse心跳
 *
 * @author zhajianjun
 * @date 2024-05-23 10:44
 */
@Component
public class SseHeatBeatTask extends AbstracTask<String> {

    private final SseHandle sseHandle;

    public SseHeatBeatTask(Environment environment, TiTraceProperty tiTraceProperty, TaskLogRepository taskLogRepository, SseHandle sseHandle) {
        super(environment, tiTraceProperty, taskLogRepository);
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
