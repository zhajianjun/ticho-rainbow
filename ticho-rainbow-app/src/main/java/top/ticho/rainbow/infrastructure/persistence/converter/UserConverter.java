package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.infrastructure.persistence.po.UserPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:21
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    List<User> toEntitys(List<UserPO> list);

    User toEntity(UserPO userPO);

    UserPO toPo(User user);

    List<UserPO> toPos(List<User> users);

}
