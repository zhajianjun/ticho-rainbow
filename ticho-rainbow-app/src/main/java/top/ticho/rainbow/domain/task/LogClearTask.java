package top.ticho.rainbow.domain.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.infrastructure.entity.TaskLog;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 日志清除
 *
 * @author zhajianjun
 * @date 2024-04-28 12:30
 */
@Component
public class LogClearTask extends AbstracTask<Integer> {

    @Resource
    private OpLogRepository opLogRepository;

    @Resource
    private TaskLogRepository taskLogRepository;

    @Override
    public void run(JobExecutionContext context) {
        Integer taskParam = getTaskParam(context);
        LocalDateTime dateTime = LocalDateTime.now().minusDays(taskParam);
        LambdaQueryWrapper<OpLog> wrapper = Wrappers.lambdaQuery();
        wrapper.le(OpLog::getCreateTime, dateTime);
        opLogRepository.remove(wrapper);

        LambdaQueryWrapper<TaskLog> taskWrapper = Wrappers.lambdaQuery();
        taskWrapper.le(TaskLog::getCreateTime, dateTime);
        taskLogRepository.remove(taskWrapper);
    }

    @Override
    public Class<Integer> getParamClass() {
        return Integer.class;
    }

}
