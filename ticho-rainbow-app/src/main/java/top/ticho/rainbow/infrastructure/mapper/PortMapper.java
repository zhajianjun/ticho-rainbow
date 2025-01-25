package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 端口信息 mapper
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Repository
public interface PortMapper extends TiMapper<Port> {

}