package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.command.RoleModifyCommand;
import top.ticho.rainbow.application.dto.command.RoleSaveCommand;
import top.ticho.rainbow.application.dto.excel.RoleExcelExport;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.vo.RoleModifyVO;

/**
 * 角色信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring")
public interface RoleAssembler {

    Role toEntity(RoleSaveCommand dto);

    RoleDTO toDTO(Role entity);

    RoleExcelExport toExp(Role entity);

    RoleModifyVO toVo(RoleModifyCommand roleModifyCommand);

}