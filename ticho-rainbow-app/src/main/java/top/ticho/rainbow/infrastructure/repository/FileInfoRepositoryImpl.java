package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.infrastructure.mapper.FileInfoMapper;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 文件信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Slf4j
@Service
public class FileInfoRepositoryImpl extends RootServiceImpl<FileInfoMapper, FileInfo> implements FileInfoRepository {

    @Override
    public List<FileInfo> list(FileInfoQuery query) {
        // @formatter:off
        LambdaQueryWrapper<FileInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), FileInfo::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getType()), FileInfo::getType, query.getType());
        wrapper.eq(StrUtil.isNotBlank(query.getFileName()), FileInfo::getFileName, query.getFileName());
        wrapper.eq(StrUtil.isNotBlank(query.getExt()), FileInfo::getExt, query.getExt());
        wrapper.eq(StrUtil.isNotBlank(query.getPath()), FileInfo::getPath, query.getPath());
        wrapper.eq(Objects.nonNull(query.getSize()), FileInfo::getSize, query.getSize());
        wrapper.eq(StrUtil.isNotBlank(query.getContentType()), FileInfo::getContentType, query.getContentType());
        wrapper.eq(StrUtil.isNotBlank(query.getOriginalFileName()), FileInfo::getOriginalFileName, query.getOriginalFileName());
        wrapper.eq(Objects.nonNull(query.getStatus()), FileInfo::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), FileInfo::getRemark, query.getRemark());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), FileInfo::getCreateBy, query.getCreateBy());
        if (Objects.nonNull(query.getCreateTime()) && query.getCreateTime().length == 2) {
            wrapper.ge(FileInfo::getCreateTime, query.getCreateTime()[0]);
            wrapper.le(FileInfo::getCreateTime, query.getCreateTime()[1]);
        }
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), FileInfo::getUpdateBy, query.getUpdateBy());
        if (Objects.nonNull(query.getUpdateTime()) && query.getUpdateTime().length == 2) {
            wrapper.ge(FileInfo::getUpdateTime, query.getUpdateTime()[0]);
            wrapper.le(FileInfo::getUpdateTime, query.getUpdateTime()[1]);
        }
        return list(wrapper);
        // @formatter:on
    }

    @Override
    public FileInfo getByChunkId(String chunkId) {
        LambdaQueryWrapper<FileInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FileInfo::getChunkId, chunkId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<FileInfo> listChunkFile(LocalDateTime before, Integer size) {
        if (Objects.isNull(before)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<FileInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FileInfo::getStatus, 3);
        wrapper.le(FileInfo::getCreateTime, before);
        wrapper.orderByAsc(FileInfo::getCreateTime);
        wrapper.last(StrUtil.format("limit {}", size));
        return list(wrapper);
    }

    @Override
    public boolean removeChunkFile(Long id) {
        LambdaUpdateWrapper<FileInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfo::getId, id);
        wrapper.eq(FileInfo::getStatus, 3);
        return remove(wrapper);
    }

}
