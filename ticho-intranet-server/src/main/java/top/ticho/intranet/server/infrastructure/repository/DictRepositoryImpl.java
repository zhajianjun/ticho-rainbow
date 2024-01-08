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
        wrapper.eq(Objects.nonNull(query.getParentId()), Dict::getPid, query.getParentId());
        wrapper.eq(Objects.nonNull(query.getTypeId()), Dict::getTypeId, query.getTypeId());
        wrapper.eq(StrUtil.isNotBlank(query.getCode()), Dict::getCode, query.getCode());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), Dict::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getSort()), Dict::getSort, query.getSort());
        wrapper.eq(Objects.nonNull(query.getLevel()), Dict::getLevel, query.getLevel());
        wrapper.eq(StrUtil.isNotBlank(query.getStructure()), Dict::getStructure, query.getStructure());
        wrapper.eq(Objects.nonNull(query.getStatus()), Dict::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), Dict::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), Dict::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), Dict::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), Dict::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), Dict::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), Dict::getUpdateTime, query.getUpdateTime());
        wrapper.eq(Objects.nonNull(query.getIsDelete()), Dict::getIsDelete, query.getIsDelete());
        return list(wrapper);
    }

    @Override
    public boolean existsByTypeId(Long typeId) {
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Dict::getId);
        wrapper.gt(Dict::getTypeId, typeId);
        wrapper.last("limit 1");
        return getOne(wrapper) != null;
    }

    @Override
    public List<Long> getDescendantIds(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.select(Dict::getId);
        wrapper.gt(Dict::getId, id);
        wrapper.like(Dict::getStructure, id);
        return listObjs(wrapper, x -> Long.valueOf(x.toString()));
    }

    @Override
    public List<Dict> getBrothers(Long id) {
        if (id == null) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Dict> wrapper = Wrappers.lambdaQuery();
        wrapper.inSql(Dict::getPid, String.format("select pid from sys_dict where id = %s", id));
        return list(wrapper);
    }

}
