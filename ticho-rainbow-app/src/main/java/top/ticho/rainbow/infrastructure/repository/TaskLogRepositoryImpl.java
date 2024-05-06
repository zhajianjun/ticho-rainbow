package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.TaskLogRepository;
import top.ticho.rainbow.infrastructure.entity.TaskLog;
import top.ticho.rainbow.infrastructure.mapper.TaskLogMapper;
import top.ticho.rainbow.interfaces.query.TaskLogQuery;

import java.util.List;
import java.util.Objects;

/**
 * 计划任务日志信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-05-06 16:41
 */
@Slf4j
@Service
public class TaskLogRepositoryImpl extends RootServiceImpl<TaskLogMapper, TaskLog> implements TaskLogRepository {

    @Override
    public List<TaskLog> list(TaskLogQuery query) {
        // @formatter:off
        LambdaQueryWrapper<TaskLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), TaskLog::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getTaskId()), TaskLog::getTaskId, query.getTaskId());
        wrapper.like(StrUtil.isNotBlank(query.getContent()), TaskLog::getContent, query.getContent());
        wrapper.like(StrUtil.isNotBlank(query.getParam()), TaskLog::getParam, query.getParam());
        if (Objects.nonNull(query.getExecuteTime()) && query.getExecuteTime().length == 2) {
            wrapper.ge(TaskLog::getExecuteTime, query.getExecuteTime()[0]);
            wrapper.le(TaskLog::getExecuteTime, query.getExecuteTime()[1]);
        }
        if (Objects.nonNull(query.getStartTime()) && query.getStartTime().length == 2) {
            wrapper.ge(TaskLog::getStartTime, query.getStartTime()[0]);
            wrapper.le(TaskLog::getStartTime, query.getStartTime()[1]);
        }
        if (Objects.nonNull(query.getEndTime()) && query.getEndTime().length == 2) {
            wrapper.ge(TaskLog::getEndTime, query.getEndTime()[0]);
            wrapper.le(TaskLog::getEndTime, query.getEndTime()[1]);
        }
        wrapper.eq(Objects.nonNull(query.getConsume()), TaskLog::getConsume, query.getConsume());
        wrapper.eq(StrUtil.isNotBlank(query.getTraceId()), TaskLog::getTraceId, query.getTraceId());
        wrapper.eq(Objects.nonNull(query.getStatus()), TaskLog::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getOperateBy()), TaskLog::getOperateBy, query.getOperateBy());
        wrapper.eq(Objects.nonNull(query.getIsErr()), TaskLog::getIsErr, query.getIsErr());
        wrapper.eq(StrUtil.isNotBlank(query.getErrMessage()), TaskLog::getErrMessage, query.getErrMessage());
        return list(wrapper);
        // @formatter:on
    }

}
