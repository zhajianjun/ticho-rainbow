package top.ticho.rainbow.application.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.executor.IntranetExecutor;

/**
 * 内网映射数据刷新定时任务
 *
 * @author zhajianjun
 * @date 2024-04-03 15:16
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class IntranetTask extends AbstracTask<String> {
    private final IntranetExecutor intranetExecutor;


    @Override
    public void run(JobExecutionContext context) {
        intranetExecutor.flush();
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
