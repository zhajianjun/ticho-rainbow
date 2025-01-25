package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.RoleMenu;
import top.ticho.starter.datasource.mapper.TiMapper;

/**
 * 角色菜单关联关系 mapper
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Repository
public interface RoleMenuMapper extends TiMapper<RoleMenu> {

}