package top.ticho.rainbow.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.ticho.boot.view.log.HttpLog;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.excel.OpLogExp;

/**
 * 日志信息 转换
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Mapper
public interface OpLogAssembler {
    OpLogAssembler INSTANCE = Mappers.getMapper(OpLogAssembler.class);

    /**
     * 日志信息
     *
     * @param httpLog 日志信息DTO
     * @return {@link OpLog}
     */
    @Mapping(ignore = true, target = "createTime")
    OpLog toEntity(HttpLog httpLog);

    /**
     * 日志信息DTO
     *
     * @param entity 日志信息
     * @return {@link OpLogDTO}
     */
    OpLogDTO entityToDto(OpLog entity);

    OpLogExp entityToExp(OpLog opLog);

}