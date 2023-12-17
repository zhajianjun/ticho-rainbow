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
        wrapper.eq(Objects.nonNull(query.getId()), Client::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getAccessKey()), Client::getAccessKey, query.getAccessKey());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), Client::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getEnabled()), Client::getEnabled, query.getEnabled());
        wrapper.eq(Objects.nonNull(query.getSort()), Client::getSort, query.getSort());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), Client::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), Client::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), Client::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), Client::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), Client::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), Client::getUpdateTime, query.getUpdateTime());
        wrapper.eq(Objects.nonNull(query.getIsDelete()), Client::getIsDelete, query.getIsDelete());
        return list(wrapper);
        // @formatter:on
    }

}
