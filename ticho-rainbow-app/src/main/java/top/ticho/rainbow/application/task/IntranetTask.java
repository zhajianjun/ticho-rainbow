package top.ticho.rainbow.application.task;

import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.executor.IntranetExecutor;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.trace.common.prop.TiTraceProperty;

/**
 * 内网映射数据刷新定时任务
 *
 * @author zhajianjun
 * @date 2024-04-03 15:16
 */
@Component
public class IntranetTask extends AbstracTask<String> {

    private final IntranetExecutor intranetExecutor;

    public IntranetTask(Environment environment, TiTraceProperty tiTraceProperty, TaskLogRepository taskLogRepository, IntranetExecutor intranetExecutor) {
        super(environment, tiTraceProperty, taskLogRepository);
        this.intranetExecutor = intranetExecutor;
    }

    @Override
    public void run(JobExecutionContext context) {
        intranetExecutor.flush();
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
