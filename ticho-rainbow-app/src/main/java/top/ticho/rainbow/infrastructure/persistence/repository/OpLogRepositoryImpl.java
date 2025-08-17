package top.ticho.rainbow.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.application.repository.OpLogAppRepository;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.OpLogConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.OpLogMapper;
import top.ticho.rainbow.infrastructure.persistence.po.OpLogPO;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.tool.core.TiCollUtil;
import top.ticho.tool.core.TiStrUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 日志信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@RequiredArgsConstructor
@Repository
public class OpLogRepositoryImpl extends TiRepositoryImpl<OpLogMapper, OpLogPO> implements OpLogRepository, OpLogAppRepository {
    private final OpLogConverter opLogConverter;

    @Override
    public void save(OpLog entity) {
        OpLogPO po = opLogConverter.toPO(entity);
        save(po);
    }

    @Override
    public OpLog find(Long id) {
        return opLogConverter.toEntity(super.getById(id));
    }

    @Override
    public TiPageResult<OpLogDTO> page(OpLogQuery query) {
        LambdaQueryWrapper<OpLogPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TiCollUtil.isNotEmpty(query.getIds()), OpLogPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), OpLogPO::getId, query.getId());
        wrapper.like(TiStrUtil.isNotBlank(query.getName()), OpLogPO::getName, query.getName());
        wrapper.like(TiStrUtil.isNotBlank(query.getUrl()), OpLogPO::getUrl, query.getUrl());
        wrapper.like(TiStrUtil.isNotBlank(query.getType()), OpLogPO::getType, query.getType());
        wrapper.like(TiStrUtil.isNotBlank(query.getReqBody()), OpLogPO::getReqBody, query.getReqBody());
        wrapper.like(TiStrUtil.isNotBlank(query.getReqParams()), OpLogPO::getReqParams, query.getReqParams());
        wrapper.like(TiStrUtil.isNotBlank(query.getResBody()), OpLogPO::getResBody, query.getResBody());
        if (Objects.nonNull(query.getEndTime()) && query.getEndTime().length == 2) {
            wrapper.ge(OpLogPO::getEndTime, query.getEndTime()[0]);
            wrapper.le(OpLogPO::getEndTime, query.getEndTime()[1]);
        }
        if (Objects.nonNull(query.getStartTime()) && query.getStartTime().length == 2) {
            wrapper.ge(OpLogPO::getStartTime, query.getStartTime()[0]);
            wrapper.le(OpLogPO::getStartTime, query.getStartTime()[1]);
        }
        wrapper.ge(Objects.nonNull(query.getConsumeStart()), OpLogPO::getConsume, query.getConsumeStart());
        wrapper.le(Objects.nonNull(query.getConsumeEnd()), OpLogPO::getConsume, query.getConsumeEnd());
        wrapper.eq(TiStrUtil.isNotBlank(query.getTraceId()), OpLogPO::getTraceId, query.getTraceId());
        wrapper.like(TiStrUtil.isNotBlank(query.getIp()), OpLogPO::getIp, query.getIp());
        wrapper.like(Objects.nonNull(query.getResStatus()), OpLogPO::getResStatus, query.getResStatus());
        wrapper.eq(TiStrUtil.isNotBlank(query.getOperateBy()), OpLogPO::getOperateBy, query.getOperateBy());
        wrapper.like(TiStrUtil.isNotBlank(query.getErrMessage()), OpLogPO::getErrMessage, query.getErrMessage());
        wrapper.eq(Objects.nonNull(query.getIsErr()), OpLogPO::getIsErr, query.getIsErr());
        wrapper.orderByDesc(OpLogPO::getId);
        return TiPageUtil.page(() -> list(wrapper), query, opLogConverter::toDTO);
    }

    @Override
    public void removeBefeoreDays(Integer days) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        LambdaQueryWrapper<OpLogPO> wrapper = Wrappers.lambdaQuery();
        wrapper.le(OpLogPO::getCreateTime, dateTime);
        remove(wrapper);
    }

}
