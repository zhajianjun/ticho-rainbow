package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.Client;
import top.ticho.intranet.server.interfaces.dto.ClientDTO;
import top.ticho.tool.intranet.server.entity.ClientInfo;

/**
 * 客户端信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper
public interface ClientAssembler {
    ClientAssembler INSTANCE = Mappers.getMapper(ClientAssembler.class);

    /**
     * 客户端信息
     *
     * @param dto 客户端信息DTO
     * @return {@link Client}
     */
    Client dtoToEntity(ClientDTO dto);

    /**
     * 客户端信息DTO
     *
     * @param entity 客户端信息
     * @return {@link ClientDTO}
     */
    ClientDTO entityToDto(Client entity);


    /**
     * 客户端信息Info
     *
     * @param entity 客户端信息
     * @return {@link ClientInfo}
     */
    ClientInfo entityToInfo(Client entity);

}