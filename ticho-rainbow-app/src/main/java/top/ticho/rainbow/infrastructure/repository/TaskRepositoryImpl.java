package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.entity.Task;
import top.ticho.rainbow.infrastructure.mapper.TaskMapper;
import top.ticho.rainbow.interfaces.query.TaskQuery;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 计划任务信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Slf4j
@Service
public class TaskRepositoryImpl extends RootServiceImpl<TaskMapper, Task> implements TaskRepository {

    @Override
    public List<Task> list(TaskQuery query) {
        LambdaQueryWrapper<Task> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), Task::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), Task::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getName()), Task::getName, query.getName());
        wrapper.like(StrUtil.isNotBlank(query.getContent()), Task::getContent, query.getContent());
        wrapper.like(StrUtil.isNotBlank(query.getParam()), Task::getParam, query.getParam());
        wrapper.like(StrUtil.isNotBlank(query.getCronExpression()), Task::getCronExpression, query.getCronExpression());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), Task::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getStatus()), Task::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), Task::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), Task::getCreateTime, query.getCreateTime());
        return list(wrapper);
    }

    @Override
    public boolean updateStatusBatch(Collection<Long> ids, Integer status) {
        if (CollUtil.isEmpty(ids) || Objects.isNull(status)) {
            return false;
        }
        LambdaUpdateWrapper<Task> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(Task::getId, ids);
        wrapper.set(Task::getStatus, status);
        return update(wrapper);
    }

}
