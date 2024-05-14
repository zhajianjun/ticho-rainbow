package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.infrastructure.entity.Client;
import top.ticho.rainbow.infrastructure.mapper.ClientMapper;
import top.ticho.rainbow.interfaces.query.ClientQuery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 客户端信息 repository实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Slf4j
@Service
public class ClientRepositoryImpl extends RootServiceImpl<ClientMapper, Client> implements ClientRepository {

    @Override
    public List<Client> list(ClientQuery query) {
        // @formatter:off
        LambdaQueryWrapper<Client> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), Client::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), Client::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getAccessKey()), Client::getAccessKey, query.getAccessKey());
        wrapper.like(StrUtil.isNotBlank(query.getName()), Client::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getStatus()), Client::getStatus, query.getStatus());
        wrapper.ge(Objects.nonNull(query.getExpireAt()), Client::getExpireAt, query.getExpireAt());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), Client::getRemark, query.getRemark());
        wrapper.orderByAsc(Client::getSort);
        wrapper.orderByDesc(Client::getId);
        return list(wrapper);
        // @formatter:on
    }

    @Override
    public Client getByAccessKey(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return null;
        }
        LambdaQueryWrapper<Client> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Client::getAccessKey, accessKey);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<Client> getByAccessKeys(List<String> accessKeys) {
        if (CollUtil.isEmpty(accessKeys)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<Client> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Client::getAccessKey, accessKeys);
        return list(wrapper);
    }

    @Override
    public void removeByAccessKey(String accessKey) {
        if (StrUtil.isBlank(accessKey)) {
            return;
        }
        LambdaQueryWrapper<Client> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Client::getAccessKey, accessKey);
        remove(wrapper);
    }

}
