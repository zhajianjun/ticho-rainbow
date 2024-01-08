package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.DictTypeRepository;
import top.ticho.intranet.server.infrastructure.entity.DictType;
import top.ticho.intranet.server.infrastructure.mapper.DictTypeMapper;
import top.ticho.intranet.server.interfaces.query.DictTypeQuery;

import java.util.List;
import java.util.Objects;

/**
 * 数据字典类型 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class DictTypeRepositoryImpl extends RootServiceImpl<DictTypeMapper, DictType> implements DictTypeRepository {

    @Override
    public List<DictType> list(DictTypeQuery query) {
        LambdaQueryWrapper<DictType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), DictType::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getCode()), DictType::getCode, query.getCode());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), DictType::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getIsSys()), DictType::getIsSys, query.getIsSys());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), DictType::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), DictType::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), DictType::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), DictType::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), DictType::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), DictType::getUpdateTime, query.getUpdateTime());
        wrapper.eq(Objects.nonNull(query.getIsDelete()), DictType::getIsDelete, query.getIsDelete());
        return list(wrapper);
    }

}
