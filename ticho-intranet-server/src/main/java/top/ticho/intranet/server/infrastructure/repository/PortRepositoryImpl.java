package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ticho.boot.datasource.service.impl.RootServiceImpl;
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
        wrapper.eq(Objects.nonNull(query.getId()), Port::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getAccessKey()), Port::getAccessKey, query.getAccessKey());
        wrapper.eq(Objects.nonNull(query.getPort()), Port::getPort, query.getPort());
        wrapper.eq(StrUtil.isNotBlank(query.getEndpoint()), Port::getEndpoint, query.getEndpoint());
        wrapper.eq(StrUtil.isNotBlank(query.getDomain()), Port::getDomain, query.getDomain());
        wrapper.eq(Objects.nonNull(query.getEnabled()), Port::getEnabled, query.getEnabled());
        wrapper.eq(Objects.nonNull(query.getForever()), Port::getForever, query.getForever());
        wrapper.eq(Objects.nonNull(query.getExpireAt()), Port::getExpireAt, query.getExpireAt());
        wrapper.eq(Objects.nonNull(query.getType()), Port::getType, query.getType());
        wrapper.eq(Objects.nonNull(query.getSort()), Port::getSort, query.getSort());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), Port::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), Port::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), Port::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), Port::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), Port::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), Port::getUpdateTime, query.getUpdateTime());
        wrapper.eq(Objects.nonNull(query.getIsDelete()), Port::getIsDelete, query.getIsDelete());
        return list(wrapper);
        // @formatter:on
    }

}
