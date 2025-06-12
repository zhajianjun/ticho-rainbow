package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.infrastructure.persistence.po.UserPO;
import top.ticho.rainbow.interfaces.dto.response.UserDTO;

import java.util.Collection;
import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:21
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    List<User> toEntity(Collection<UserPO> list);

    User toEntity(UserPO userPO);

    UserPO toPo(User user);

    List<UserPO> toPo(Collection<User> users);

    UserDTO toDTO(UserPO userPO);

}
