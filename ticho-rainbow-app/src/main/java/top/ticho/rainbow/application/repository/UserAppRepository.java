package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.query.UserQuery;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:29
 */
public interface UserAppRepository {

    TiPageResult<UserDTO> page(UserQuery query);

}
