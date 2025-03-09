package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.query.TaskLogQuery;
import top.ticho.rainbow.domain.entity.TaskLog;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.TaskLogConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.TaskLogMapper;
import top.ticho.rainbow.infrastructure.persistence.po.OpLogPO;
import top.ticho.rainbow.infrastructure.persistence.po.TaskLogPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 计划任务日志信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Service
@RequiredArgsConstructor
public class TaskLogRepositoryImpl extends TiRepositoryImpl<TaskLogMapper, TaskLogPO> implements TaskLogRepository {
    private final TaskLogConverter taskLogConverter;

    @Override
    public boolean save(TaskLog taskLog) {
        TaskLogPO taskLogPo = taskLogConverter.toPo(taskLog);
        return save(taskLogPo);
    }

    @Override
    public List<TaskLog> list(TaskLogQuery query) {
        LambdaQueryWrapper<TaskLogPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), TaskLogPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), TaskLogPO::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getTaskId()), TaskLogPO::getTaskId, query.getTaskId());
        wrapper.like(StrUtil.isNotBlank(query.getContent()), TaskLogPO::getContent, query.getContent());
        wrapper.like(StrUtil.isNotBlank(query.getParam()), TaskLogPO::getParam, query.getParam());
        if (Objects.nonNull(query.getExecuteTime()) && query.getExecuteTime().length == 2) {
            wrapper.ge(TaskLogPO::getExecuteTime, query.getExecuteTime()[0]);
            wrapper.le(TaskLogPO::getExecuteTime, query.getExecuteTime()[1]);
        }
        if (Objects.nonNull(query.getStartTime()) && query.getStartTime().length == 2) {
            wrapper.ge(TaskLogPO::getStartTime, query.getStartTime()[0]);
            wrapper.le(TaskLogPO::getStartTime, query.getStartTime()[1]);
        }
        if (Objects.nonNull(query.getEndTime()) && query.getEndTime().length == 2) {
            wrapper.ge(TaskLogPO::getEndTime, query.getEndTime()[0]);
            wrapper.le(TaskLogPO::getEndTime, query.getEndTime()[1]);
        }
        wrapper.ge(Objects.nonNull(query.getConsumeStart()), TaskLogPO::getConsume, query.getConsumeStart());
        wrapper.le(Objects.nonNull(query.getConsumeEnd()), TaskLogPO::getConsume, query.getConsumeEnd());
        wrapper.like(StrUtil.isNotBlank(query.getTraceId()), TaskLogPO::getTraceId, query.getTraceId());
        wrapper.eq(Objects.nonNull(query.getStatus()), TaskLogPO::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getOperateBy()), TaskLogPO::getOperateBy, query.getOperateBy());
        wrapper.eq(Objects.nonNull(query.getIsErr()), TaskLogPO::getIsErr, query.getIsErr());
        wrapper.like(StrUtil.isNotBlank(query.getErrMessage()), TaskLogPO::getErrMessage, query.getErrMessage());
        wrapper.orderByDesc(TaskLogPO::getId);
        return taskLogConverter.toEntitys(list(wrapper));
    }

    @Override
    public TaskLog find(Long id) {
        return taskLogConverter.toEntity(getById(id));
    }

    public void removeBefeoreDays(Integer days) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        LambdaQueryWrapper<TaskLogPO> wrapper = Wrappers.lambdaQuery();
        wrapper.le(TaskLogPO::getCreateTime, dateTime);
        remove(wrapper);
    }

}
