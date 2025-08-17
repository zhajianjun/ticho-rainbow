package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.intranet.server.entity.IntranetClient;
import top.ticho.rainbow.application.dto.excel.ClientExcelExport;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.vo.ClientModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.interfaces.command.ClientModifyCommand;
import top.ticho.rainbow.interfaces.command.ClientSaveCommand;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.tool.core.TiIdUtil;

/**
 * 客户端信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class, CommonStatus.class})
public interface ClientAssembler {

    @Mapping(target = "status", expression = "java(CommonStatus.DISABLE.code())")
    @Mapping(target = "id", expression = "java(TiIdUtil.snowId())")
    @Mapping(target = "accessKey", expression = "java(TiIdUtil.ulid())")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Client toEntity(ClientSaveCommand dto);

    ClientModifyVO toVO(ClientModifyCommand dto);

    @Mapping(target = "connectTime", ignore = true)
    @Mapping(target = "channelStatus", ignore = true)
    ClientDTO toDTO(Client client);

    @Mapping(target = "statusName", ignore = true)
    @Mapping(target = "connectTime", ignore = true)
    @Mapping(target = "channelStatusName", ignore = true)
    ClientExcelExport toExcelExport(ClientDTO clientDTO);

    @Mapping(target = "accessKey", source = "accessKey")
    @Mapping(target = "name", source = "name")
    IntranetClient toIntranetClient(Client client);

}