package top.ticho.rainbow.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;

/**
 * 日志信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper
public interface OpLogAssembler {
    OpLogAssembler INSTANCE = Mappers.getMapper(OpLogAssembler.class);

    /**
     * 日志信息
     *
     * @param dto 日志信息DTO
     * @return {@link OpLog}
     */
    OpLog dtoToEntity(OpLogDTO dto);

    /**
     * 日志信息DTO
     *
     * @param entity 日志信息
     * @return {@link OpLogDTO}
     */
    OpLogDTO entityToDto(OpLog entity);

}