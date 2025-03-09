package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.UserDTO;
import top.ticho.rainbow.application.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.excel.UserExp;
import top.ticho.rainbow.application.dto.excel.UserImp;
import top.ticho.rainbow.application.dto.query.UserAccountQuery;
import top.ticho.rainbow.domain.entity.User;

/**
 * 用户信息 转换
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Mapper(componentModel = "spring")
public interface UserAssembler {

    /**
     * 用户信息
     *
     * @param dto 用户信息DTO
     * @return {@link User}
     */
    User toEntity(UserDTO dto);

    /**
     * 用户信息DTO
     *
     * @param entity 用户信息
     * @return {@link UserDTO}
     */
    UserDTO toDTO(User entity);

    /**
     * 用户转用户登录账号信息
     *
     * @param user 用户
     * @return {@link UserAccountQuery}
     */
    @Mapping(target = "status", ignore = true)
    UserAccountQuery toAccountQuery(User user);

    UserRoleMenuDtlDTO toDtlDTO(User user);

    UserExp toExp(User user);

    User toEntity(UserImp imp);
}