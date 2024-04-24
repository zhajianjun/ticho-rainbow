package top.ticho.rainbow.domain.task;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.core.component.AbstracTask;
import top.ticho.rainbow.infrastructure.core.component.CacheTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;
import top.ticho.rainbow.infrastructure.entity.FileInfo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 内网映射数据刷新定时任务
 *
 * @author zhajianjun
 * @date 2024-04-03 15:16
 */
@Component
public class FileInfoClearTask extends AbstracTask<Integer> {

    @Autowired
    private FileProperty fileProperty;

    @Autowired
    private CacheTemplate cacheTemplate;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Override
    public void run(JobExecutionContext context) {
        // TODO 清除过期的分片数据
        // Date previousFireTime = context.getPreviousFireTime();
        // LocalDateTime before = Optional.ofNullable(DateUtil.toLocalDateTime(previousFireTime)).orElse(LocalDateTime.now());
        // Integer size = Optional.ofNullable(getTaskParam(context, Integer.class)).orElse(20);
        // List<FileInfo> fileInfos = fileInfoRepository.listChunkFile(before, size);
        // for (FileInfo fileInfo : fileInfos) {
        //     boolean removeChunkFile = fileInfoRepository.removeChunkFile(fileInfo.getId());
        // }
    }

}
