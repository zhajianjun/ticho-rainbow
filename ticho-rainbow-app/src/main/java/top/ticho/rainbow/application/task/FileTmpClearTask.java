package top.ticho.rainbow.application.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.common.prop.FileProperty;
import top.ticho.tool.core.TiFileUtil;

import java.io.File;

/**
 * 临时文件清除定时任务
 *
 * @author zhajianjun
 * @date 2024-04-25 17:51
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FileTmpClearTask extends AbstracTask<String> {
    private final FileProperty fileProperty;


    @Override
    public void run(JobExecutionContext context) {
        String tmpPath = fileProperty.getTmpPath();
        File file = new File(tmpPath);
        if (!file.exists()) {
            log.info("临时文件路径不存在，无需清除，路径：{}", tmpPath);
            return;
        }
        TiFileUtil.del(file);
    }

    @Override
    public Class<String> getParamClass() {
        return String.class;
    }

}
