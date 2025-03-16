package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.query.ClientQuery;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.ClientConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.ClientMapper;
import top.ticho.rainbow.infrastructure.persistence.po.ClientPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 客户端信息 repository实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
@RequiredArgsConstructor
public class ClientRepositoryImpl extends TiRepositoryImpl<ClientMapper, ClientPO> implements ClientRepository {
    private final ClientConverter clientConverter;

    @Override
    public boolean save(Client client) {
        return save(clientConverter.toPo(client));
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(Client client) {
        return updateById(clientConverter.toPo(client));
    }

    @Override
    public Client find(Long id) {
        return clientConverter.toEntity(super.getById(id));
    }

    @Override
    public List<Client> list(ClientQuery query) {
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
        return clientConverter.toEntitys(list(wrapper));
    }

    @Override
    public TiPageResult<Client> page(ClientQuery query) {
        query.checkPage();
        Page<ClientPO> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), query.getCount());
        page.doSelectPage(() -> list(query));
        return TiPageUtil.of(page, clientConverter::toEntity);
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
        return clientConverter.toEntitys(list(wrapper));
    }

    @Override
    public void removeByAccessKey(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return;
        }
        LambdaQueryWrapper<ClientPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ClientPO::getAccessKey, accessKey);
        remove(wrapper);
    }

}
