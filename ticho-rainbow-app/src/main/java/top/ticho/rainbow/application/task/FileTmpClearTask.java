package top.ticho.rainbow.application.task;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;
import top.ticho.trace.common.prop.TraceProperty;

import java.io.File;

/**
 * 临时文件清除定时任务
 *
 * @author zhajianjun
 * @date 2024-04-25 17:51
 */
@Slf4j
@Component
public class FileTmpClearTask extends AbstracTask<String> {

    private final FileProperty fileProperty;
    public FileTmpClearTask(Environment environment, TraceProperty traceProperty, TaskLogRepository taskLogRepository, FileProperty fileProperty) {
        super(environment, traceProperty, taskLogRepository);
        this.fileProperty = fileProperty;
    }

    @Override
    public void run(JobExecutionContext context) {
        String tmpPath = fileProperty.getTmpPath();
        File file = new File(tmpPath);
        if (!file.exists()) {
            log.info("临时文件路径不存在，无需清除，路径：{}", tmpPath);
            return;
        }
        FileUtil.del(file);
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
