package top.ticho.rainbow.application.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.service.SseHandle;

/**
 * sse心跳
 *
 * @author zhajianjun
 * @date 2024-05-23 10:44
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SseHeatBeatTask extends AbstracTask<String> {
    private final SseHandle sseHandle;


    @Override
    public void run(JobExecutionContext context) {
        sseHandle.heatbeat();
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
