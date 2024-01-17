package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.DictRepository;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.infrastructure.mapper.DictMapper;
import top.ticho.intranet.server.interfaces.query.DictQuery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 数据字典 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class DictRepositoryImpl extends RootServiceImpl<DictMapper, Dict> implements DictRepository {

    @Override
    public List<Dict> list(DictQuery query) {
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), Dict::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getCode()), Dict::getCode, query.getCode());
        wrapper.eq(StrUtil.isNotBlank(query.getLabel()), Dict::getLabel, query.getLabel());
        wrapper.eq(StrUtil.isNotBlank(query.getValue()), Dict::getValue, query.getValue());
        wrapper.eq(Objects.nonNull(query.getSort()), Dict::getSort, query.getSort());
        wrapper.eq(Objects.nonNull(query.getStatus()), Dict::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), Dict::getRemark, query.getRemark());
        wrapper.orderByAsc(Dict::getSort);
        wrapper.orderByDesc(Dict::getId);
        return list(wrapper);
    }

    @Override
    public List<Dict> getByCode(String code) {
        if (StrUtil.isBlank(code)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Dict::getCode, code);
        wrapper.orderByAsc(Dict::getSort);
        wrapper.orderByDesc(Dict::getId);
        return list(wrapper);
    }

    @Override
    public boolean existsByCode(String code) {
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Dict::getId);
        wrapper.eq(Dict::getCode, code);
        wrapper.last("limit 1");
        return getOne(wrapper) != null;
    }

    @Override
    public Dict getByCodeAndValueExcludeId(String code, String value, Long excludeId) {
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Dict::getCode, code);
        wrapper.eq(Dict::getValue, value);
        wrapper.ne(Objects.nonNull(excludeId), Dict::getId, excludeId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

}
