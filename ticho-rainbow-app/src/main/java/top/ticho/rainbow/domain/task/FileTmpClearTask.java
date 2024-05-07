package top.ticho.rainbow.domain.task;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;

import java.io.File;

/**
 * 临时文件清除定时任务
 *
 * @author zhajianjun
 * @date 2024-04-25 17:51
 */
@Component
@Slf4j
public class FileTmpClearTask extends AbstracTask<String> {

    @Autowired
    private FileProperty fileProperty;

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
