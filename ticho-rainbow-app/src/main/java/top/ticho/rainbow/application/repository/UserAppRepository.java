package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.query.UserQuery;
import top.ticho.rainbow.interfaces.dto.response.UserDTO;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:29
 */
public interface UserAppRepository {

    TiPageResult<UserDTO> page(UserQuery query);

}
