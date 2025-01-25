package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.infrastructure.entity.DictLabel;
import top.ticho.rainbow.infrastructure.mapper.DictLabelMapper;
import top.ticho.rainbow.interfaces.query.DictLabelQuery;
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
@Slf4j
@Service
public class DictLabelRepositoryImpl extends TiRepositoryImpl<DictLabelMapper, DictLabel> implements DictLabelRepository {

    @Override
    public List<DictLabel> list(DictLabelQuery query) {
        LambdaQueryWrapper<DictLabel> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), DictLabel::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getCode()), DictLabel::getCode, query.getCode());
        wrapper.eq(StrUtil.isNotBlank(query.getLabel()), DictLabel::getLabel, query.getLabel());
        wrapper.eq(StrUtil.isNotBlank(query.getValue()), DictLabel::getValue, query.getValue());
        wrapper.eq(Objects.nonNull(query.getSort()), DictLabel::getSort, query.getSort());
        wrapper.eq(Objects.nonNull(query.getStatus()), DictLabel::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), DictLabel::getRemark, query.getRemark());
        wrapper.orderByAsc(DictLabel::getSort);
        wrapper.orderByDesc(DictLabel::getId);
        return list(wrapper);
    }

    @Override
    public List<DictLabel> getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<DictLabel> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictLabel::getCode, code);
        wrapper.orderByAsc(DictLabel::getSort);
        wrapper.orderByDesc(DictLabel::getId);
        return list(wrapper);
    }

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<DictLabel> wrapper = Wrappers.lambdaQuery();
        wrapper.select(DictLabel::getId);
        wrapper.eq(DictLabel::getCode, code);
        wrapper.last("limit 1");
        return getOne(wrapper) != null;
    }

    @Override
    public DictLabel getByCodeAndValueExcludeId(String code, String value, Long excludeId) {
        LambdaQueryWrapper<DictLabel> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictLabel::getCode, code);
        wrapper.eq(DictLabel::getValue, value);
        wrapper.ne(Objects.nonNull(excludeId), DictLabel::getId, excludeId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

}
