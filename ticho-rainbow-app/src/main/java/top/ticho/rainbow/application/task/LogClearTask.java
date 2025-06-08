package top.ticho.rainbow.application.task;

import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.trace.common.prop.TiTraceProperty;

/**
 * 日志清除
 *
 * @author zhajianjun
 * @date 2024-04-28 12:30
 */
@Component
public class LogClearTask extends AbstracTask<Integer> {

    private final OpLogRepository opLogRepository;
    private final TaskLogRepository taskLogRepository;

    public LogClearTask(Environment environment, TiTraceProperty tiTraceProperty, OpLogRepository opLogRepository, TaskLogRepository taskLogRepository) {
        super(environment, tiTraceProperty, taskLogRepository);
        this.opLogRepository = opLogRepository;
        this.taskLogRepository = taskLogRepository;
    }


    @Override
    public void run(JobExecutionContext context) {
        Integer taskParam = getTaskParam(context);
        opLogRepository.removeBefeoreDays(taskParam);
        taskLogRepository.removeBefeoreDays(taskParam);
    }

    @Override
    public Class<Integer> getParamClass() {
        return Integer.class;
    }

}
