package top.ticho.rainbow.domain.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.entity.OpLog;

import java.time.LocalDateTime;

/**
 * 日志清除
 *
 * @author zhajianjun
 * @date 2024-04-28 12:30
 */
@Component
public class LogClearTask extends AbstracTask<Integer> {

    @Autowired
    private OpLogRepository opLogRepository;

    @Override
    public void run(JobExecutionContext context) {
        Integer taskParam = getTaskParam(context, Integer.class);
        LocalDateTime dateTime = LocalDateTime.now().minusDays(taskParam);
        LambdaQueryWrapper<OpLog> wrapper = Wrappers.lambdaQuery();
        wrapper.le(OpLog::getCreateTime, dateTime);
        opLogRepository.remove(wrapper);
    }
}
