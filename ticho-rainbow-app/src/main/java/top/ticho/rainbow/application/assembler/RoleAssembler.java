package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.command.RoleModifyCommand;
import top.ticho.rainbow.application.dto.command.RoleSaveCommand;
import top.ticho.rainbow.application.dto.excel.RoleExcelExport;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.vo.RoleModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.web.util.TiIdUtil;

/**
 * 角色信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class, CommonStatus.class})
public interface RoleAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "status", expression = "java(CommonStatus.DISABLE.code())")
    Role toEntity(RoleSaveCommand dto);

    RoleDTO toDTO(Role entity);

    RoleExcelExport toExcelExport(RoleDTO roleDTO);

    RoleModifyVO toVo(RoleModifyCommand roleModifyCommand);

}