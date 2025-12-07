package top.ticho.rainbow.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.application.repository.DictAppRepository;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.infrastructure.persistence.converter.DictConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.DictMapper;
import top.ticho.rainbow.infrastructure.persistence.po.DictPO;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.query.DictQuery;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.tool.core.TiCollUtil;
import top.ticho.tool.core.TiStrUtil;

import java.util.List;
import java.util.Objects;

/**
 * 字典 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Repository
public class DictRepositoryImpl extends TiRepositoryImpl<DictMapper, DictPO> implements DictRepository, DictAppRepository {
    private final DictConverter dictConverter;

    @Override
    public boolean save(Dict dict) {
        return save(dictConverter.toPO(dict));
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(Dict dict) {
        return updateById(dictConverter.toPO(dict));
    }

    @Override
    public boolean modifyBatch(List<Dict> roles) {
        return super.updateBatchById(dictConverter.toPO(roles));
    }

    @Override
    public Dict find(Long id) {
        return dictConverter.toEntity(super.getById(id));
    }

    @Override
    public List<Dict> list(List<Long> ids) {
        return dictConverter.toEntity(listByIds(ids));
    }

    @Override
    public List<Dict> listEnable() {
        LambdaQueryWrapper<DictPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictPO::getStatus, CommonStatus.ENABLE.code());
        wrapper.orderByDesc(DictPO::getId);
        return dictConverter.toEntity(list(wrapper));
    }

    @Override
    public TiPageResult<DictDTO> page(DictQuery query) {
        query.checkPage();
        LambdaQueryWrapper<DictPO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(TiStrUtil.isNotBlank(query.getRemark()), DictPO::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getId()), DictPO::getId, query.getId());
        wrapper.in(TiCollUtil.isNotEmpty(query.getIds()), DictPO::getId, query.getIds());
        wrapper.like(TiStrUtil.isNotBlank(query.getCode()), DictPO::getCode, query.getCode());
        wrapper.like(TiStrUtil.isNotBlank(query.getName()), DictPO::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getIsSys()), DictPO::getIsSys, query.getIsSys());
        wrapper.eq(Objects.nonNull(query.getStatus()), DictPO::getStatus, query.getStatus());
        wrapper.orderByDesc(DictPO::getId);
        Page<DictPO> page = new Page<>(query.getPageNum(), query.getPageSize(), query.getCount());
        page(page, wrapper);
        return TiPageUtil.of(page, dictConverter::toDTO);
    }

    @Override
    public Dict getByCodeExcludeId(String code, Long excludeId) {
        LambdaQueryWrapper<DictPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictPO::getCode, code);
        wrapper.ne(Objects.nonNull(excludeId), DictPO::getId, excludeId);
        wrapper.last("limit 1");
        return dictConverter.toEntity(getOne(wrapper));
    }

}
