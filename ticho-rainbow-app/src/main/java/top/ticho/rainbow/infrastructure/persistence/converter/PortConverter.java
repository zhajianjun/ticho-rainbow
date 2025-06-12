package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.infrastructure.persistence.po.PortPO;
import top.ticho.rainbow.interfaces.dto.response.PortDTO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:19
 */
@Mapper(componentModel = "spring")
public interface PortConverter {

    List<Port> toEntity(List<PortPO> portPOS);

    Port toEntity(PortPO portPO);

    PortPO toPo(Port port);

    List<PortPO> toPo(List<Port> port);

    PortDTO toDTO(PortPO portPO);

}
