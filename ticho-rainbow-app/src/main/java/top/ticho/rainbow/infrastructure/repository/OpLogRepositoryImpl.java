package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.infrastructure.mapper.OpLogMapper;
import top.ticho.rainbow.interfaces.query.OpLogQuery;

import java.util.List;
import java.util.Objects;

/**
 * 日志信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Slf4j
@Service
public class OpLogRepositoryImpl extends RootServiceImpl<OpLogMapper, OpLog> implements OpLogRepository {

    @Override
    public List<OpLog> list(OpLogQuery query) {
        LambdaQueryWrapper<OpLog> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), OpLog::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), OpLog::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getName()), OpLog::getName, query.getName());
        wrapper.like(StrUtil.isNotBlank(query.getUrl()), OpLog::getUrl, query.getUrl());
        wrapper.like(StrUtil.isNotBlank(query.getType()), OpLog::getType, query.getType());
        wrapper.like(StrUtil.isNotBlank(query.getReqBody()), OpLog::getReqBody, query.getReqBody());
        wrapper.like(StrUtil.isNotBlank(query.getReqParams()), OpLog::getReqParams, query.getReqParams());
        wrapper.like(StrUtil.isNotBlank(query.getResBody()), OpLog::getResBody, query.getResBody());
        if (Objects.nonNull(query.getStartTime()) && query.getStartTime().length == 2) {
            wrapper.ge(OpLog::getStartTime, query.getStartTime()[0]);
            wrapper.le(OpLog::getStartTime, query.getStartTime()[1]);
        }
        if (Objects.nonNull(query.getEndTime()) && query.getEndTime().length == 2) {
            wrapper.ge(OpLog::getEndTime, query.getEndTime()[0]);
            wrapper.le(OpLog::getEndTime, query.getEndTime()[1]);
        }
        wrapper.ge(Objects.nonNull(query.getConsumeStart()), OpLog::getConsume, query.getConsumeStart());
        wrapper.le(Objects.nonNull(query.getConsumeEnd()), OpLog::getConsume, query.getConsumeEnd());
        wrapper.eq(StrUtil.isNotBlank(query.getTraceId()), OpLog::getTraceId, query.getTraceId());
        wrapper.like(StrUtil.isNotBlank(query.getIp()), OpLog::getIp, query.getIp());
        wrapper.like(Objects.nonNull(query.getResStatus()), OpLog::getResStatus, query.getResStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getOperateBy()), OpLog::getOperateBy, query.getOperateBy());
        wrapper.eq(Objects.nonNull(query.getIsErr()), OpLog::getIsErr, query.getIsErr());
        wrapper.like(StrUtil.isNotBlank(query.getErrMessage()), OpLog::getErrMessage, query.getErrMessage());
        wrapper.orderByDesc(OpLog::getId);
        return list(wrapper);
    }

}
