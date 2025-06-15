package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.repository.TaskAppRepository;
import top.ticho.rainbow.domain.entity.Task;
import top.ticho.rainbow.domain.repository.TaskRepository;
import top.ticho.rainbow.infrastructure.persistence.converter.TaskConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.TaskMapper;
import top.ticho.rainbow.infrastructure.persistence.po.TaskPO;
import top.ticho.rainbow.interfaces.query.TaskQuery;
import top.ticho.rainbow.interfaces.dto.TaskDTO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;
import java.util.Objects;

/**
 * 计划任务信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-03-23 23:38
 */
@Service
@RequiredArgsConstructor
public class TaskRepositoryImpl extends TiRepositoryImpl<TaskMapper, TaskPO> implements TaskRepository, TaskAppRepository {
    private final TaskConverter taskConverter;

    @Override
    public boolean save(Task task) {
        TaskPO taskPO = taskConverter.toPo(task);
        return save(taskPO);
    }

    @Override
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    public boolean modify(Task task) {
        TaskPO taskPO = taskConverter.toPo(task);
        return updateById(taskPO);
    }

    @Override
    public boolean modifyBatch(List<Task> tasks) {
        return super.updateBatchById(taskConverter.toPo(tasks));
    }

    @Override
    public Task find(Long id) {
        return taskConverter.toEntity(super.getById(id));
    }

    @Override
    public List<Task> list(List<Long> ids) {
        return taskConverter.toEntity(super.listByIds(ids));
    }

    @Override
    public TiPageResult<TaskDTO> page(TaskQuery query) {
        LambdaQueryWrapper<TaskPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), TaskPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), TaskPO::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getName()), TaskPO::getName, query.getName());
        wrapper.like(StrUtil.isNotBlank(query.getContent()), TaskPO::getContent, query.getContent());
        wrapper.like(StrUtil.isNotBlank(query.getParam()), TaskPO::getParam, query.getParam());
        wrapper.like(StrUtil.isNotBlank(query.getCronExpression()), TaskPO::getCronExpression, query.getCronExpression());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), TaskPO::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getStatus()), TaskPO::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), TaskPO::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), TaskPO::getCreateTime, query.getCreateTime());
        return TiPageUtil.page(() -> list(wrapper), query, taskConverter::toDTO);
    }

    @Override
    public List<Task> all() {
        return taskConverter.toEntity(list());
    }

}
