package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.TaskLog;
import top.ticho.rainbow.infrastructure.persistence.po.TaskLogPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:21
 */
@Mapper(componentModel = "spring")
public interface TaskLogConverter {

    List<TaskLog> toEntitys(List<TaskLogPO> list);

    TaskLog toEntity(TaskLogPO taskLogPO);

    TaskLogPO toPo(TaskLog taskLog);

}
