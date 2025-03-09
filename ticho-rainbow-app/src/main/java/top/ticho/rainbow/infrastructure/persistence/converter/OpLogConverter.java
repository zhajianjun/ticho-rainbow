package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.infrastructure.persistence.po.OpLogPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:18
 */
@Mapper(componentModel = "spring")
public interface OpLogConverter {

    List<OpLog> toEntitys(List<OpLogPO> opLogPOs);

    OpLog toEntity(OpLogPO opLogPO);

    OpLogPO toPo(OpLog entity);

}
