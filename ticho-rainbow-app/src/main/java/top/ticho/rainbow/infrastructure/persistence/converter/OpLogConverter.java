package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.OpLog;
import top.ticho.rainbow.infrastructure.persistence.po.OpLogPO;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:18
 */
@Mapper(componentModel = "spring")
public interface OpLogConverter {

    OpLog toEntity(OpLogPO opLogPO);

    OpLogPO toPo(OpLog entity);

}
