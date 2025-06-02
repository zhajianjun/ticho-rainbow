package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.application.repository.PortAppRepository;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.PortConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.PortMapper;
import top.ticho.rainbow.infrastructure.persistence.po.PortPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

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
@Service
@RequiredArgsConstructor
public class PortRepositoryImpl extends TiRepositoryImpl<PortMapper, PortPO> implements PortRepository, PortAppRepository {
    private final PortConverter portConverter;

    @Override
    public boolean save(Port port) {
        PortPO portPO = portConverter.toPo(port);
        return save(portPO);
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(Port port) {
        PortPO portPO = portConverter.toPo(port);
        return updateById(portPO);
    }

    @Override
    public boolean modifyBatch(List<Port> ports) {
        return super.updateBatchById(portConverter.toPo(ports));
    }

    @Override
    public Port find(Long id) {
        return portConverter.toEntity(super.getById(id));
    }

    @Override
    public List<Port> list(List<Long> ids) {
        return portConverter.toEntity(super.listByIds(ids));
    }

    @Override
    public TiPageResult<PortDTO> page(PortQuery query) {
        LambdaQueryWrapper<PortPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), PortPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), PortPO::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getAccessKey()), PortPO::getAccessKey, query.getAccessKey());
        wrapper.eq(Objects.nonNull(query.getPort()), PortPO::getPort, query.getPort());
        wrapper.like(StrUtil.isNotBlank(query.getEndpoint()), PortPO::getEndpoint, query.getEndpoint());
        wrapper.eq(Objects.nonNull(query.getStatus()), PortPO::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getDomain()), PortPO::getDomain, query.getDomain());
        wrapper.eq(Objects.nonNull(query.getType()), PortPO::getType, query.getType());
        wrapper.ge(Objects.nonNull(query.getExpireAt()), PortPO::getExpireAt, query.getExpireAt());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), PortPO::getRemark, query.getRemark());
        wrapper.orderByAsc(PortPO::getSort);
        wrapper.orderByAsc(PortPO::getPort);
        return TiPageUtil.page(() -> list(wrapper), query, portConverter::toDTO);
    }

    @Override
    public List<PortDTO> all() {
        return list()
            .stream()
            .map(portConverter::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Port getByPortExcludeId(Long excludeId, Integer port) {
        if (Objects.isNull(port)) {
            return null;
        }
        LambdaQueryWrapper<PortPO> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Objects.nonNull(excludeId), PortPO::getId, excludeId);
        wrapper.eq(PortPO::getPort, port);
        wrapper.last("limit 1");
        return portConverter.toEntity(getOne(wrapper));
    }

    @Override
    public Port getByDomainExcludeId(Long excludeId, String domain) {
        if (StrUtil.isBlank(domain)) {
            return null;
        }
        LambdaQueryWrapper<PortPO> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Objects.nonNull(excludeId), PortPO::getId, excludeId);
        wrapper.eq(PortPO::getDomain, domain);
        wrapper.last("limit 1");
        return portConverter.toEntity(getOne(wrapper));
    }

    @Override
    public List<Port> listByAccessKeys(Collection<String> accessKeys) {
        if (CollUtil.isEmpty(accessKeys)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PortPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(PortPO::getAccessKey, accessKeys);
        wrapper.orderByAsc(PortPO::getSort);
        wrapper.orderByAsc(PortPO::getPort);
        return portConverter.toEntity(list(wrapper));
    }

    @Override
    public <T> Map<String, List<T>> listAndGroupByAccessKey(Collection<String> accessKeys, Function<Port, T> function, Predicate<Port> filter) {
        if (Objects.isNull(function) || Objects.isNull(filter)) {
            return Collections.emptyMap();
        }
        return listByAccessKeys(accessKeys)
            .stream()
            .filter(filter)
            .collect(Collectors.groupingBy(Port::getAccessKey, Collectors.mapping(function, Collectors.toList())));
    }


}
