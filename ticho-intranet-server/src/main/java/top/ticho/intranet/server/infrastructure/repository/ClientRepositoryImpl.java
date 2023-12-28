package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ticho.boot.datasource.service.impl.RootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.domain.repository.ClientRepository;
import top.ticho.intranet.server.infrastructure.entity.Client;
import top.ticho.intranet.server.infrastructure.mapper.ClientMapper;
import top.ticho.intranet.server.interfaces.query.ClientQuery;

import java.util.List;

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
        wrapper.like(StrUtil.isNotBlank(query.getAccessKey()), Client::getAccessKey, query.getAccessKey());
        wrapper.like(StrUtil.isNotBlank(query.getName()), Client::getName, query.getName());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), Client::getRemark, query.getRemark());
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

}
