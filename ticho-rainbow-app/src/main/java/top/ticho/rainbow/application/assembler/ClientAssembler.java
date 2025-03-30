package top.ticho.rainbow.application.assembler;

import cn.hutool.core.util.IdUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import top.ticho.rainbow.application.dto.command.ClientModifyCommand;
import top.ticho.rainbow.application.dto.command.ClientSaveCommand;
import top.ticho.rainbow.application.dto.excel.ClientExcelExport;
import top.ticho.rainbow.application.dto.response.ClientDTO;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.vo.ClientModifyVO;
import top.ticho.starter.web.util.TiIdUtil;
import top.ticho.tool.intranet.server.entity.ClientInfo;

/**
 * 客户端信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring", imports = {IdUtil.class, TiIdUtil.class})
public interface ClientAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "accessKey", expression = "java(IdUtil.fastSimpleUUID())", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    Client toEntity(ClientSaveCommand dto);

    ClientModifyVO toVO(ClientModifyCommand dto);

    @Mapping(target = "connectTime", ignore = true)
    @Mapping(target = "channelStatus", ignore = true)
    ClientDTO toDTO(Client entity);

    @Mapping(target = "portMap", ignore = true)
    @Mapping(target = "connectTime", ignore = true)
    @Mapping(target = "channel", ignore = true)
    ClientInfo toInfo(Client entity);

    @Mapping(target = "statusName", ignore = true)
    @Mapping(target = "connectTime", ignore = true)
    @Mapping(target = "channelStatusName", ignore = true)
    ClientExcelExport toExp(Client client);

}