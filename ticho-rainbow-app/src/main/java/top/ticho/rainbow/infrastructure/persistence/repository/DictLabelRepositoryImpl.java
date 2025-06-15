package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
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
@RequiredArgsConstructor
@Repository
public class DictLabelRepositoryImpl extends TiRepositoryImpl<DictLabelMapper, DictLabelPO> implements DictLabelRepository {
    private final DictLabelConverter dictLabelConverter;

    @Override
    public boolean save(DictLabel dictLabel) {
        DictLabelPO po = dictLabelConverter.toPO(dictLabel);
        return save(po);
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(DictLabel dictLabel) {
        DictLabelPO po = dictLabelConverter.toPO(dictLabel);
        return updateById(po);
    }

    @Override
    public boolean modifyBatch(List<DictLabel> dictLabels) {
        return super.updateBatchById(dictLabelConverter.toPO(dictLabels));
    }

    @Override
    public DictLabel find(Long id) {
        return dictLabelConverter.toEntity(super.getById(id));
    }

    @Override
    public List<DictLabel> list(List<Long> ids) {
        return dictLabelConverter.toEntity(listByIds(ids));
    }

    public List<DictLabel> listEnable() {
        LambdaQueryWrapper<DictLabelPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictLabelPO::getStatus, CommonStatus.ENABLE.code());
        wrapper.orderByAsc(DictLabelPO::getSort);
        wrapper.orderByDesc(DictLabelPO::getId);
        return dictLabelConverter.toEntity(list(wrapper));
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
        return dictLabelConverter.toEntity(list(wrapper));
    }

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<DictLabelPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DictLabelPO::getCode, code);
        wrapper.last("limit 1");
        return count(wrapper) >= 1;
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
