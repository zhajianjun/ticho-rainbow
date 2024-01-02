package top.ticho.intranet.server.infrastructure.mapper;

import top.ticho.boot.datasource.mapper.RootMapper;
import org.springframework.stereotype.Repository;
import top.ticho.intranet.server.infrastructure.entity.Port;


/**
 * 端口信息 mapper
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Repository
public interface PortMapper extends RootMapper<Port> {

}