package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.command.PortModifyfCommand;
import top.ticho.rainbow.application.dto.command.PortSaveCommand;
import top.ticho.rainbow.application.dto.excel.PortExp;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.PortModifyfVO;
import top.ticho.tool.intranet.server.entity.PortInfo;

/**
 * 端口信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring")
public interface PortAssembler {

    Port toEntity(PortSaveCommand dto);

    PortDTO toDTO(Port entity);

    PortInfo toInfo(Port entity);

    PortExp toExp(Port port);

    PortModifyfVO toModifyfVO(PortModifyfCommand portModifyfCommand);

}