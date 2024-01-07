package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.core.server.entity.PortInfo;
import top.ticho.intranet.server.infrastructure.entity.Port;
import top.ticho.intranet.server.interfaces.dto.PortDTO;

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

}