package top.ticho.rainbow.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.application.repository.FileInfoAppRepository;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.FileInfoConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.FileInfoMapper;
import top.ticho.rainbow.infrastructure.persistence.po.FileInfoPO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.tool.core.TiCollUtil;
import top.ticho.tool.core.TiStrUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 文件信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@RequiredArgsConstructor
@Repository
public class FileInfoRepositoryImpl extends TiRepositoryImpl<FileInfoMapper, FileInfoPO> implements FileInfoRepository, FileInfoAppRepository {
    private final FileInfoConverter fileInfoConverter;

    @Override
    public boolean save(FileInfo fileInfo) {
        FileInfoPO fileInfoPO = fileInfoConverter.toPO(fileInfo);
        return save(fileInfoPO);
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(FileInfo fileInfo) {
        FileInfoPO fileInfoPO = fileInfoConverter.toPO(fileInfo);
        return updateById(fileInfoPO);
    }

    @Override
    public boolean modifyBatch(List<FileInfo> fileInfos) {
        return super.updateBatchById(fileInfoConverter.toPO(fileInfos));
    }

    @Override
    public List<FileInfo> list(List<Long> ids) {
        return fileInfoConverter.toEntity(super.listByIds(ids));
    }

    @Override
    public FileInfo find(Long id) {
        FileInfoPO po = super.getById(id);
        return fileInfoConverter.toEntity(po);
    }

    @Override
    public TiPageResult<FileInfoDTO> page(FileInfoQuery query) {
        LambdaQueryWrapper<FileInfoPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TiCollUtil.isNotEmpty(query.getIds()), FileInfoPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), FileInfoPO::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getType()), FileInfoPO::getType, query.getType());
        wrapper.like(TiStrUtil.isNotBlank(query.getFileName()), FileInfoPO::getFileName, query.getFileName());
        wrapper.like(TiStrUtil.isNotBlank(query.getExt()), FileInfoPO::getExt, query.getExt());
        wrapper.like(TiStrUtil.isNotBlank(query.getPath()), FileInfoPO::getPath, query.getPath());
        wrapper.ge(Objects.nonNull(query.getSizeStart()), FileInfoPO::getSize, query.getSizeStart());
        wrapper.le(Objects.nonNull(query.getSizeEnd()), FileInfoPO::getSize, query.getSizeEnd());
        wrapper.like(TiStrUtil.isNotBlank(query.getContentType()), FileInfoPO::getContentType, query.getContentType());
        wrapper.like(TiStrUtil.isNotBlank(query.getOriginalFileName()), FileInfoPO::getOriginalFileName, query.getOriginalFileName());
        wrapper.eq(Objects.nonNull(query.getStatus()), FileInfoPO::getStatus, query.getStatus());
        wrapper.like(TiStrUtil.isNotBlank(query.getRemark()), FileInfoPO::getRemark, query.getRemark());
        wrapper.like(TiStrUtil.isNotBlank(query.getCreateBy()), FileInfoPO::getCreateBy, query.getCreateBy());
        if (Objects.nonNull(query.getCreateTime()) && query.getCreateTime().length == 2) {
            wrapper.ge(FileInfoPO::getCreateTime, query.getCreateTime()[0]);
            wrapper.le(FileInfoPO::getCreateTime, query.getCreateTime()[1]);
        }
        wrapper.eq(TiStrUtil.isNotBlank(query.getUpdateBy()), FileInfoPO::getUpdateBy, query.getUpdateBy());
        if (Objects.nonNull(query.getUpdateTime()) && query.getUpdateTime().length == 2) {
            wrapper.ge(FileInfoPO::getUpdateTime, query.getUpdateTime()[0]);
            wrapper.le(FileInfoPO::getUpdateTime, query.getUpdateTime()[1]);
        }
        wrapper.orderByDesc(FileInfoPO::getId);
        return TiPageUtil.page(() -> list(wrapper), query, fileInfoConverter::toDTO);
    }

    public static <T, R> TiPageResult<R> page(Supplier<List<T>> supplier, TiPageQuery query, Function<T, R> function) {
        Page<T> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), query.getCount());
        page.doSelectPage(supplier::get);
        TiPageResult<R> tiPageResult = new TiPageResult<>();
        tiPageResult.setPageNum(page.getPageNum());
        tiPageResult.setPageSize(page.getPageSize());
        tiPageResult.setPages(page.getPages());
        tiPageResult.setTotal(Long.valueOf(page.getTotal()).intValue());
        tiPageResult.setRows(page.getResult().stream().map(function).collect(Collectors.toList()));
        return tiPageResult;
    }

    @Override
    public FileInfo getByChunkId(String chunkId) {
        LambdaQueryWrapper<FileInfoPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FileInfoPO::getChunkId, chunkId);
        wrapper.last("limit 1");
        return fileInfoConverter.toEntity(getOne(wrapper));
    }

}
