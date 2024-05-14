package top.ticho.rainbow.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.rainbow.interfaces.dto.PortDTO;
import top.ticho.rainbow.interfaces.excel.PortExp;
import top.ticho.tool.intranet.server.entity.PortInfo;

/**
 * 端口信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper
public interface PortAssembler {
    PortAssembler INSTANCE = Mappers.getMapper(PortAssembler.class);

    /**
     * 端口信息
     *
     * @param dto 端口信息DTO
     * @return {@link Port}
     */
    Port dtoToEntity(PortDTO dto);

    /**
     * 端口信息DTO
     *
     * @param entity 端口信息
     * @return {@link PortDTO}
     */
    PortDTO entityToDto(Port entity);

    /**
     * 端口信息Info
     *
     * @param entity 端口信息
     * @return {@link PortInfo}
     */
    PortInfo entityToInfo(Port entity);

    PortExp entityToExp(Port port);
}