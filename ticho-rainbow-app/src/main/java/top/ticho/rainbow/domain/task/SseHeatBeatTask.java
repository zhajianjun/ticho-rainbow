package top.ticho.rainbow.domain.task;

import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.handle.SseHandle;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;

import javax.annotation.Resource;

/**
 * sse心跳
 *
 * @author zhajianjun
 * @date 2024-05-23 10:44
 */
@Component
public class SseHeatBeatTask extends AbstracTask<String> {

    @Resource
    private SseHandle sseHandle;

    @Override
    public void run(JobExecutionContext context) {
        sseHandle.heatbeat();
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
