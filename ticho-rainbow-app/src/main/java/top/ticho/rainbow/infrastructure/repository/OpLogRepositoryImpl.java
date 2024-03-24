package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.infrastructure.mapper.OpLogMapper;
import top.ticho.rainbow.interfaces.query.OpLogQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        // @formatter:off
        LambdaQueryWrapper<OpLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), OpLog::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), OpLog::getName, query.getName());
        wrapper.eq(StrUtil.isNotBlank(query.getUrl()), OpLog::getUrl, query.getUrl());
        wrapper.eq(StrUtil.isNotBlank(query.getType()), OpLog::getType, query.getType());
        wrapper.eq(StrUtil.isNotBlank(query.getPosition()), OpLog::getPosition, query.getPosition());
        wrapper.eq(StrUtil.isNotBlank(query.getReqBody()), OpLog::getReqBody, query.getReqBody());
        wrapper.eq(StrUtil.isNotBlank(query.getReqParams()), OpLog::getReqParams, query.getReqParams());
        wrapper.eq(StrUtil.isNotBlank(query.getReqHeaders()), OpLog::getReqHeaders, query.getReqHeaders());
        wrapper.eq(StrUtil.isNotBlank(query.getResBody()), OpLog::getResBody, query.getResBody());
        wrapper.eq(StrUtil.isNotBlank(query.getResHeaders()), OpLog::getResHeaders, query.getResHeaders());
        wrapper.eq(Objects.nonNull(query.getStartTime()), OpLog::getStartTime, query.getStartTime());
        wrapper.eq(Objects.nonNull(query.getEndTime()), OpLog::getEndTime, query.getEndTime());
        wrapper.eq(Objects.nonNull(query.getConsume()), OpLog::getConsume, query.getConsume());
        wrapper.eq(StrUtil.isNotBlank(query.getIp()), OpLog::getIp, query.getIp());
        wrapper.eq(Objects.nonNull(query.getResStatus()), OpLog::getResStatus, query.getResStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getOperateBy()), OpLog::getOperateBy, query.getOperateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), OpLog::getCreateTime, query.getCreateTime());
        wrapper.eq(Objects.nonNull(query.getIsErr()), OpLog::getIsErr, query.getIsErr());
        wrapper.eq(StrUtil.isNotBlank(query.getErrMessage()), OpLog::getErrMessage, query.getErrMessage());
        return list(wrapper);
        // @formatter:on
    }

}
