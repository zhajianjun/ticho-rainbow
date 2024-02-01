package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.rainbow.infrastructure.mapper.PortMapper;
import top.ticho.rainbow.interfaces.query.PortQuery;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 端口信息 repository实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Slf4j
@Service
public class PortRepositoryImpl extends RootServiceImpl<PortMapper, Port> implements PortRepository {

    @Override
    public List<Port> list(PortQuery query) {
        // @formatter:off
        LambdaQueryWrapper<Port> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getAccessKey()), Port::getAccessKey, query.getAccessKey());
        wrapper.eq(Objects.nonNull(query.getPort()), Port::getPort, query.getPort());
        wrapper.like(StrUtil.isNotBlank(query.getEndpoint()), Port::getEndpoint, query.getEndpoint());
        wrapper.like(StrUtil.isNotBlank(query.getDomain()), Port::getDomain, query.getDomain());
        wrapper.eq(Objects.nonNull(query.getType()), Port::getType, query.getType());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), Port::getRemark, query.getRemark());
        wrapper.orderByAsc(Port::getSort);
        wrapper.orderByAsc(Port::getPort);
        return list(wrapper);
        // @formatter:on
    }

    @Override
    public Port getByPortExcludeId(Long excludeId, Integer port) {
        if (Objects.isNull(port)) {
            return null;
        }
        LambdaQueryWrapper<Port> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Objects.nonNull(excludeId), Port::getId, excludeId);
        wrapper.eq(Port::getPort, port);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public Port getByDomainExcludeId(Long excludeId, String domain) {
        if (StrUtil.isBlank(domain)) {
            return null;
        }
        LambdaQueryWrapper<Port> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Objects.nonNull(excludeId), Port::getId, excludeId);
        wrapper.eq(Port::getDomain, domain);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<Port> listByAccessKeys(Collection<String> accessKeys) {
        if (CollUtil.isEmpty(accessKeys)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Port> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Port::getAccessKey, accessKeys);
        wrapper.orderByAsc(Port::getSort);
        wrapper.orderByAsc(Port::getPort);
        return list(wrapper);
    }

    @Override
    public <T> Map<String, List<T>> listAndGroupByAccessKey(Collection<String> accessKeys, Function<Port, T> function, Predicate<Port> filter) {
        // @formatter:off
        if (Objects.isNull(function) || Objects.isNull(filter)) {
            return Collections.emptyMap();
        }
        return listByAccessKeys(accessKeys)
            .stream()
            .filter(filter)
            .collect(Collectors.groupingBy(Port::getAccessKey, Collectors.mapping(function, Collectors.toList())));
        // @formatter:on
    }


}
