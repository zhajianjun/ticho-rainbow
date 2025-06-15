package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.repository.ClientAppRepository;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.infrastructure.persistence.converter.ClientConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.ClientMapper;
import top.ticho.rainbow.infrastructure.persistence.po.ClientPO;
import top.ticho.rainbow.interfaces.query.ClientQuery;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户端信息 repository实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
@RequiredArgsConstructor
public class ClientRepositoryImpl extends TiRepositoryImpl<ClientMapper, ClientPO> implements ClientRepository, ClientAppRepository {
    private final ClientConverter clientConverter;

    @Override
    public boolean save(Client client) {
        return super.save(clientConverter.toPo(client));
    }

    @Override
    public boolean remove(Long id) {
        return super.removeById(id);
    }

    @Override
    public boolean modify(Client client) {
        return super.updateById(clientConverter.toPo(client));
    }

    @Override
    public boolean modifyBatch(List<Client> clients) {
        return super.updateBatchById(clientConverter.toPo(clients));
    }

    @Override
    public Client find(Long id) {
        return clientConverter.toEntity(super.getById(id));
    }

    @Override
    public List<Client> list(List<Long> ids) {
        return clientConverter.toEntity(super.listByIds(ids));
    }

    public List<ClientPO> list(ClientQuery query) {
        LambdaQueryWrapper<ClientPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), ClientPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), ClientPO::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getAccessKey()), ClientPO::getAccessKey, query.getAccessKey());
        wrapper.like(StrUtil.isNotBlank(query.getName()), ClientPO::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getStatus()), ClientPO::getStatus, query.getStatus());
        wrapper.ge(Objects.nonNull(query.getExpireAt()), ClientPO::getExpireAt, query.getExpireAt());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), ClientPO::getRemark, query.getRemark());
        wrapper.orderByAsc(ClientPO::getSort);
        wrapper.orderByDesc(ClientPO::getId);
        return list(wrapper);
    }

    @Override
    public List<ClientDTO> all() {
        return list()
            .stream()
            .map(clientConverter::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TiPageResult<ClientDTO> page(ClientQuery query) {
        return TiPageUtil.page(() -> list(query), query, clientConverter::toDTO);
    }

    @Override
    public Client findByAccessKey(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return null;
        }
        LambdaQueryWrapper<ClientPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ClientPO::getAccessKey, accessKey);
        wrapper.last("limit 1");
        return clientConverter.toEntity(getOne(wrapper));
    }

    @Override
    public List<Client> listByAccessKeys(List<String> accessKeys) {
        if (CollUtil.isEmpty(accessKeys)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ClientPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(ClientPO::getAccessKey, accessKeys);
        return clientConverter.toEntity(list(wrapper));
    }

    @Override
    public List<Client> listEffect() {
        LambdaQueryWrapper<ClientPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ClientPO::getStatus, CommonStatus.ENABLE.code());
        wrapper.ge(ClientPO::getExpireAt, LocalDateTime.now());
        return clientConverter.toEntity(list(wrapper));
    }

}
