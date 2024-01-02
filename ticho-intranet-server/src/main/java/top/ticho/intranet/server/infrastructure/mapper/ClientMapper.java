package top.ticho.intranet.server.infrastructure.mapper;

import top.ticho.boot.datasource.mapper.RootMapper;
import org.springframework.stereotype.Repository;
import top.ticho.intranet.server.infrastructure.entity.Client;


/**
 * 客户端信息 mapper
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Repository
public interface ClientMapper extends RootMapper<Client> {

}