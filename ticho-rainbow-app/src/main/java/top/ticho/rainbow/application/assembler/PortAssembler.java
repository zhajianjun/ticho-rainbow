package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.intranet.server.entity.IntranetPort;
import top.ticho.rainbow.application.dto.excel.PortExcelExport;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.PortModifyfVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.interfaces.command.PortModifyfCommand;
import top.ticho.rainbow.interfaces.command.PortSaveCommand;
import top.ticho.rainbow.interfaces.dto.PortDTO;
import top.ticho.starter.web.util.TiIdUtil;

/**
 * 端口信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class, CommonStatus.class})
public interface PortAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "status", expression = "java(CommonStatus.DISABLE.code())")
    Port toEntity(PortSaveCommand dto);

    PortDTO toDTO(Port entity);

    PortExcelExport toExcelExport(PortDTO portDTO);

    PortModifyfVO toModifyfVO(PortModifyfCommand portModifyfCommand);

    IntranetPort toIntranetPort(Port entity);

}