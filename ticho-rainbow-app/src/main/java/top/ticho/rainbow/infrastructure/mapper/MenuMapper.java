package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.Menu;
import top.ticho.starter.datasource.mapper.TiMapper;

/**
 * 菜单信息 mapper
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Repository
public interface MenuMapper extends TiMapper<Menu> {

}