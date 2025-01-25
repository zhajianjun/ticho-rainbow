package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.core.enums.FileInfoStatus;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.infrastructure.mapper.FileInfoMapper;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;

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
public class FileInfoRepositoryImpl extends TiRepositoryImpl<FileInfoMapper, FileInfo> implements FileInfoRepository {

    @Override
    public List<FileInfo> list(FileInfoQuery query) {
        LambdaQueryWrapper<FileInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), FileInfo::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), FileInfo::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getType()), FileInfo::getType, query.getType());
        wrapper.like(StrUtil.isNotBlank(query.getFileName()), FileInfo::getFileName, query.getFileName());
        wrapper.like(StrUtil.isNotBlank(query.getExt()), FileInfo::getExt, query.getExt());
        wrapper.like(StrUtil.isNotBlank(query.getPath()), FileInfo::getPath, query.getPath());
        wrapper.ge(Objects.nonNull(query.getSizeStart()), FileInfo::getSize, query.getSizeStart());
        wrapper.le(Objects.nonNull(query.getSizeEnd()), FileInfo::getSize, query.getSizeEnd());
        wrapper.like(StrUtil.isNotBlank(query.getContentType()), FileInfo::getContentType, query.getContentType());
        wrapper.like(StrUtil.isNotBlank(query.getOriginalFileName()), FileInfo::getOriginalFileName, query.getOriginalFileName());
        wrapper.eq(Objects.nonNull(query.getStatus()), FileInfo::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), FileInfo::getRemark, query.getRemark());
        wrapper.like(StrUtil.isNotBlank(query.getCreateBy()), FileInfo::getCreateBy, query.getCreateBy());
        if (Objects.nonNull(query.getCreateTime()) && query.getCreateTime().length == 2) {
            wrapper.ge(FileInfo::getCreateTime, query.getCreateTime()[0]);
            wrapper.le(FileInfo::getCreateTime, query.getCreateTime()[1]);
        }
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), FileInfo::getUpdateBy, query.getUpdateBy());
        if (Objects.nonNull(query.getUpdateTime()) && query.getUpdateTime().length == 2) {
            wrapper.ge(FileInfo::getUpdateTime, query.getUpdateTime()[0]);
            wrapper.le(FileInfo::getUpdateTime, query.getUpdateTime()[1]);
        }
        wrapper.orderByDesc(FileInfo::getId);
        return list(wrapper);
    }

    @Override
    public FileInfo getByChunkId(String chunkId) {
        LambdaQueryWrapper<FileInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FileInfo::getChunkId, chunkId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public boolean enable(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LambdaUpdateWrapper<FileInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfo::getId, id);
        wrapper.eq(FileInfo::getStatus, FileInfoStatus.DISABLED.code());
        wrapper.set(FileInfo::getStatus, FileInfoStatus.NORMAL.code());
        return update(wrapper);
    }

    @Override
    public boolean disable(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LambdaUpdateWrapper<FileInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfo::getId, id);
        wrapper.eq(FileInfo::getStatus, FileInfoStatus.NORMAL.code());
        wrapper.set(FileInfo::getStatus, FileInfoStatus.DISABLED.code());
        return update(wrapper);
    }

    @Override
    public boolean cancel(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LambdaUpdateWrapper<FileInfo> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfo::getId, id);
        wrapper.ne(FileInfo::getStatus, FileInfoStatus.CANCE.code());
        wrapper.set(FileInfo::getStatus, FileInfoStatus.CANCE.code());
        return update(wrapper);
    }


}
