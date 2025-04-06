package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.infrastructure.persistence.po.RolePO;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-03-02 17:20
 */
@Mapper(componentModel = "spring")
public interface RoleConverter {

    List<Role> toEntitys(List<RolePO> list);

    RoleDTO toDTO(RolePO rolePO);

    Role toEntity(RolePO rolePO);

    RolePO toPo(Role role);

}
