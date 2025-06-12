package top.ticho.rainbow.infrastructure.persistence.converter;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.infrastructure.persistence.po.ClientPO;
import top.ticho.rainbow.interfaces.dto.response.ClientDTO;

import java.util.Collection;
import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-01 20:07
 */
@Mapper(componentModel = "spring")
public interface ClientConverter {

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    ClientPO toPo(Client client);

    List<ClientPO> toPo(Collection<Client> client);

    List<Client> toEntity(List<ClientPO> list);

    Client toEntity(ClientPO clientPO);

    @Mapping(target = "connectTime", ignore = true)
    @Mapping(target = "channelStatus", ignore = true)
    ClientDTO toDTO(ClientPO clientPO);

}
