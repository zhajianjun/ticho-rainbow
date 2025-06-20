package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.infrastructure.persistence.po.RolePO;
import top.ticho.rainbow.interfaces.dto.RoleDTO;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-03-02 17:20
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    List<Role> toEntity(List<RolePO> list);

    RoleDTO toDTO(RolePO rolePO);

    Role toEntity(RolePO rolePO);

    RolePO toPO(Role role);

    List<RolePO> toPO(List<Role> roles);

}
