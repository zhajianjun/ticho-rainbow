package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.query.DictLabelQuery;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.DictLabelConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.DictLabelMapper;
import top.ticho.rainbow.infrastructure.persistence.po.DictLabelPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 字典标签 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
@RequiredArgsConstructor
public class DictLabelRepositoryImpl extends TiRepositoryImpl<DictLabelMapper, DictLabelPO> implements DictLabelRepository {
    private final DictLabelConverter dictLabelConverter;

    @Override
    public boolean save(DictLabel dictLabel) {
        DictLabelPO po = dictLabelConverter.toPo(dictLabel);
        return save(po);
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(DictLabel dictLabel) {
        DictLabelPO po = dictLabelConverter.toPo(dictLabel);
        return updateById(po);
    }

    @Override
    public DictLabel find(Long id) {
        return dictLabelConverter.toEntity(getById(id));
    }

    @Override
    public List<DictLabel> list(DictLabelQuery query) {
        LambdaQueryWrapper<DictLabelPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), DictLabelPO::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getCode()), DictLabelPO::getCode, query.getCode());
        wrapper.eq(StrUtil.isNotBlank(query.getLabel()), DictLabelPO::getLabel, query.getLabel());
        wrapper.eq(StrUtil.isNotBlank(query.getValue()), DictLabelPO::getValue, query.getValue());
        wrapper.eq(Objects.nonNull(query.getSort()), DictLabelPO::getSort, query.getSort());
        wrapper.eq(Objects.nonNull(query.getStatus()), DictLabelPO::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), DictLabelPO::getRemark, query.getRemark());
        wrapper.orderByAsc(DictLabelPO::getSort);
        wrapper.orderByDesc(DictLabelPO::getId);
        return dictLabelConverter.toEntitys(list(wrapper));
    }

    @Override
    public List<DictLabel> getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DictLabelPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictLabelPO::getCode, code);
        wrapper.orderByAsc(DictLabelPO::getSort);
        wrapper.orderByDesc(DictLabelPO::getId);
        return dictLabelConverter.toEntitys(list(wrapper));
    }

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<DictLabelPO> wrapper = Wrappers.lambdaQuery();
        wrapper.select(DictLabelPO::getId);
        wrapper.eq(DictLabelPO::getCode, code);
        wrapper.last("limit 1");
        return getOne(wrapper) != null;
    }

    @Override
    public DictLabel getByCodeAndValueExcludeId(String code, String value, Long excludeId) {
        LambdaQueryWrapper<DictLabelPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictLabelPO::getCode, code);
        wrapper.eq(DictLabelPO::getValue, value);
        wrapper.ne(Objects.nonNull(excludeId), DictLabelPO::getId, excludeId);
        wrapper.last("limit 1");
        return dictLabelConverter.toEntity(getOne(wrapper));
    }

}
