package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.UserRole;
import top.ticho.starter.datasource.mapper.TiMapper;

/**
 * 用户角色关联关系 mapper
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Repository
public interface UserRoleMapper extends TiMapper<UserRole> {


}