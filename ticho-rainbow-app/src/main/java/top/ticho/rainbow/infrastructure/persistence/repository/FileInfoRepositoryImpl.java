package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.query.FileInfoQuery;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.core.enums.FileInfoStatus;
import top.ticho.rainbow.infrastructure.persistence.converter.FileInfoConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.FileInfoMapper;
import top.ticho.rainbow.infrastructure.persistence.po.FileInfoPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;
import java.util.Objects;

/**
 * 文件信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Service
@RequiredArgsConstructor
public class FileInfoRepositoryImpl extends TiRepositoryImpl<FileInfoMapper, FileInfoPO> implements FileInfoRepository {
    private final FileInfoConverter fileInfoConverter;

    @Override
    public boolean save(FileInfo fileInfo) {
        FileInfoPO fileInfoPO = fileInfoConverter.toPo(fileInfo);
        return save(fileInfoPO);
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(FileInfo fileInfo) {
        FileInfoPO fileInfoPO = fileInfoConverter.toPo(fileInfo);
        return updateById(fileInfoPO);
    }

    @Override
    public FileInfo find(Long id) {
        FileInfoPO po = super.getById(id);
        return fileInfoConverter.toEntity(po);
    }

    @Override
    public List<FileInfo> list(FileInfoQuery query) {
        LambdaQueryWrapper<FileInfoPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), FileInfoPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), FileInfoPO::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getType()), FileInfoPO::getType, query.getType());
        wrapper.like(StrUtil.isNotBlank(query.getFileName()), FileInfoPO::getFileName, query.getFileName());
        wrapper.like(StrUtil.isNotBlank(query.getExt()), FileInfoPO::getExt, query.getExt());
        wrapper.like(StrUtil.isNotBlank(query.getPath()), FileInfoPO::getPath, query.getPath());
        wrapper.ge(Objects.nonNull(query.getSizeStart()), FileInfoPO::getSize, query.getSizeStart());
        wrapper.le(Objects.nonNull(query.getSizeEnd()), FileInfoPO::getSize, query.getSizeEnd());
        wrapper.like(StrUtil.isNotBlank(query.getContentType()), FileInfoPO::getContentType, query.getContentType());
        wrapper.like(StrUtil.isNotBlank(query.getOriginalFileName()), FileInfoPO::getOriginalFileName, query.getOriginalFileName());
        wrapper.eq(Objects.nonNull(query.getStatus()), FileInfoPO::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), FileInfoPO::getRemark, query.getRemark());
        wrapper.like(StrUtil.isNotBlank(query.getCreateBy()), FileInfoPO::getCreateBy, query.getCreateBy());
        if (Objects.nonNull(query.getCreateTime()) && query.getCreateTime().length == 2) {
            wrapper.ge(FileInfoPO::getCreateTime, query.getCreateTime()[0]);
            wrapper.le(FileInfoPO::getCreateTime, query.getCreateTime()[1]);
        }
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), FileInfoPO::getUpdateBy, query.getUpdateBy());
        if (Objects.nonNull(query.getUpdateTime()) && query.getUpdateTime().length == 2) {
            wrapper.ge(FileInfoPO::getUpdateTime, query.getUpdateTime()[0]);
            wrapper.le(FileInfoPO::getUpdateTime, query.getUpdateTime()[1]);
        }
        wrapper.orderByDesc(FileInfoPO::getId);
        return fileInfoConverter.toEntitys(list(wrapper));
    }

    @Override
    public TiPageResult<FileInfo> page(FileInfoQuery query) {
        query.checkPage();
        Page<FileInfoPO> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), query.getCount());
        page.doSelectPage(() -> list(query));
        return TiPageUtil.of(page, fileInfoConverter::toEntity);
    }

    @Override
    public FileInfo getByChunkId(String chunkId) {
        LambdaQueryWrapper<FileInfoPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FileInfoPO::getChunkId, chunkId);
        wrapper.last("limit 1");
        return fileInfoConverter.toEntity(getOne(wrapper));
    }

    @Override
    public boolean enable(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LambdaUpdateWrapper<FileInfoPO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfoPO::getId, id);
        wrapper.eq(FileInfoPO::getStatus, FileInfoStatus.DISABLED.code());
        wrapper.set(FileInfoPO::getStatus, FileInfoStatus.NORMAL.code());
        return update(wrapper);
    }

    @Override
    public boolean disable(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LambdaUpdateWrapper<FileInfoPO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfoPO::getId, id);
        wrapper.eq(FileInfoPO::getStatus, FileInfoStatus.NORMAL.code());
        wrapper.set(FileInfoPO::getStatus, FileInfoStatus.DISABLED.code());
        return update(wrapper);
    }

    @Override
    public boolean cancel(Long id) {
        if (Objects.isNull(id)) {
            return false;
        }
        LambdaUpdateWrapper<FileInfoPO> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(FileInfoPO::getId, id);
        wrapper.ne(FileInfoPO::getStatus, FileInfoStatus.CANCE.code());
        wrapper.set(FileInfoPO::getStatus, FileInfoStatus.CANCE.code());
        return update(wrapper);
    }


}
