package top.ticho.rainbow.infrastructure.persistence.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.persistence.po.RolePO;
import top.ticho.starter.datasource.mapper.TiMapper;

/**
 * 角色信息 mapper
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Repository
public interface RoleMapper extends TiMapper<RolePO> {

}