package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.Role;
import top.ticho.intranet.server.interfaces.dto.RoleDTO;

import java.util.List;

/**
 * 角色信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper
public interface RoleAssembler {
    RoleAssembler INSTANCE = Mappers.getMapper(RoleAssembler.class);

    /**
     * 角色信息
     *
     * @param dto 角色信息DTO
     * @return {@link Role}
     */
    Role dtoToEntity(RoleDTO dto);

    /**
     * 角色信息DTO
     *
     * @param entity 角色信息
     * @return {@link RoleDTO}
     */
    RoleDTO entityToDto(Role entity);

    /**
     * 角色信息DTO
     *
     * @param roles 角色信息
     * @return {@link RoleDTO}
     */
    List<RoleDTO> entityToDtos(List<Role> roles);

}