package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.interfaces.dto.query.DictQuery;
import top.ticho.rainbow.interfaces.dto.response.DictDTO;
import top.ticho.rainbow.application.repository.DictAppRepository;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.infrastructure.persistence.converter.DictConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.DictMapper;
import top.ticho.rainbow.infrastructure.persistence.po.DictPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;
import java.util.Objects;

/**
 * 字典 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
@RequiredArgsConstructor
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
        LambdaQueryWrapper<DictPO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), DictPO::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getId()), DictPO::getId, query.getId());
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), DictPO::getId, query.getIds());
        wrapper.like(StrUtil.isNotBlank(query.getCode()), DictPO::getCode, query.getCode());
        wrapper.like(StrUtil.isNotBlank(query.getName()), DictPO::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getIsSys()), DictPO::getIsSys, query.getIsSys());
        wrapper.eq(Objects.nonNull(query.getStatus()), DictPO::getStatus, query.getStatus());
        wrapper.orderByDesc(DictPO::getId);
        return TiPageUtil.page(() -> list(wrapper), query, dictConverter::toDTO);
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
