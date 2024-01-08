package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.OpLogRepository;
import top.ticho.intranet.server.infrastructure.entity.OpLog;
import top.ticho.intranet.server.infrastructure.mapper.OpLogMapper;
import top.ticho.intranet.server.interfaces.query.OpLogQuery;

import java.util.List;
import java.util.Objects;

/**
 * 日志信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class OpLogRepositoryImpl extends RootServiceImpl<OpLogMapper, OpLog> implements OpLogRepository {

    @Override
    public List<OpLog> list(OpLogQuery query) {
        LambdaQueryWrapper<OpLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), OpLog::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getUrl()), OpLog::getUrl, query.getUrl());
        wrapper.eq(StrUtil.isNotBlank(query.getType()), OpLog::getType, query.getType());
        wrapper.eq(StrUtil.isNotBlank(query.getMethod()), OpLog::getMethod, query.getMethod());
        wrapper.eq(StrUtil.isNotBlank(query.getParams()), OpLog::getParams, query.getParams());
        wrapper.eq(StrUtil.isNotBlank(query.getMessage()), OpLog::getMessage, query.getMessage());
        wrapper.eq(Objects.nonNull(query.getTotalTime()), OpLog::getTotalTime, query.getTotalTime());
        wrapper.eq(StrUtil.isNotBlank(query.getIp()), OpLog::getIp, query.getIp());
        wrapper.eq(StrUtil.isNotBlank(query.getOperateBy()), OpLog::getOperateBy, query.getOperateBy());
        wrapper.eq(Objects.nonNull(query.getOperateTime()), OpLog::getOperateTime, query.getOperateTime());
        wrapper.eq(Objects.nonNull(query.getIsErr()), OpLog::getIsErr, query.getIsErr());
        wrapper.eq(StrUtil.isNotBlank(query.getErrMessage()), OpLog::getErrMessage, query.getErrMessage());
        return list(wrapper);
    }

}
