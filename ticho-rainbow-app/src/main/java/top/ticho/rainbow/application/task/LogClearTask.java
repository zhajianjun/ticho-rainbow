package top.ticho.rainbow.application.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.domain.repository.TaskLogRepository;

/**
 * 日志清除
 *
 * @author zhajianjun
 * @date 2024-04-28 12:30
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LogClearTask extends AbstracTask<Integer> {
    private final OpLogRepository opLogRepository;
    private final TaskLogRepository taskLogRepository;


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
