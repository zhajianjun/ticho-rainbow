package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.query.RoleQuery;
import top.ticho.rainbow.interfaces.dto.response.RoleDTO;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:14
 */
public interface RoleAppRepository {

    TiPageResult<RoleDTO> page(RoleQuery query);

    List<RoleDTO> all();

}
