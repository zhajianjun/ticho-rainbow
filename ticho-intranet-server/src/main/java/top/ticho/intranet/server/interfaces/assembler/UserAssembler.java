package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.User;
import top.ticho.intranet.server.interfaces.dto.UserDTO;
import top.ticho.intranet.server.interfaces.dto.UserRoleMenuDtlDTO;
import top.ticho.intranet.server.interfaces.query.UserAccountQuery;

/**
 * 用户信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper
public interface UserAssembler {
    UserAssembler INSTANCE = Mappers.getMapper(UserAssembler.class);

    /**
     * 用户信息
     *
     * @param dto 用户信息DTO
     * @return {@link User}
     */
    User dtoToEntity(UserDTO dto);

    /**
     * 用户信息DTO
     *
     * @param entity 用户信息
     * @return {@link UserDTO}
     */
    UserDTO entityToDto(User entity);

    /**
     * 用户转用户登录账号信息
     *
     * @param user 用户
     * @return {@link UserAccountQuery}
     */
    @Mapping(target = "status", ignore = true)
    UserAccountQuery entityToAccount(User user);

    UserRoleMenuDtlDTO entityToDtl(User user);

}