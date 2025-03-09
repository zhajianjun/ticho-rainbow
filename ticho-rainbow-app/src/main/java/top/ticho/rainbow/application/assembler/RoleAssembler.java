package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.rainbow.application.dto.RoleDTO;
import top.ticho.rainbow.application.dto.command.RoleModifyCommand;
import top.ticho.rainbow.application.dto.excel.RoleExp;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.vo.RoleModifyVO;

/**
 * 角色信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring")
public interface RoleAssembler {

    Role toEntity(RoleDTO dto);

    RoleDTO toDTO(Role entity);

    RoleExp toExp(Role entity);

    RoleModifyVO toVo(RoleModifyCommand roleModifyCommand);

}