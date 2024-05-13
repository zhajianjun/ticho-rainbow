package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.entity.Dict;
import top.ticho.rainbow.infrastructure.mapper.DictMapper;
import top.ticho.rainbow.interfaces.query.DictQuery;

import java.util.List;
import java.util.Objects;

/**
 * 字典 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class DictRepositoryImpl extends RootServiceImpl<DictMapper, Dict> implements DictRepository {

    @Override
    public List<Dict> list(DictQuery query) {
        // @formatter:off
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), Dict::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), Dict::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getCode()), Dict::getCode, query.getCode());
        wrapper.like(StrUtil.isNotBlank(query.getName()), Dict::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getIsSys()), Dict::getIsSys, query.getIsSys());
        wrapper.eq(Objects.nonNull(query.getStatus()), Dict::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), Dict::getRemark, query.getRemark());
        wrapper.orderByDesc(Dict::getId);
        return list(wrapper);
        // @formatter:on
    }

    @Override
    public Dict getByCodeExcludeId(String code, Long excludeId) {
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Dict::getCode, code);
        wrapper.ne(Objects.nonNull(excludeId), Dict::getId, excludeId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

}
