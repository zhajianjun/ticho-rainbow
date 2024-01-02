package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.domain.repository.PortRepository;
import top.ticho.intranet.server.infrastructure.entity.Port;
import top.ticho.intranet.server.infrastructure.mapper.PortMapper;
import top.ticho.intranet.server.interfaces.query.PortQuery;

import java.util.List;
import java.util.Objects;

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

}
