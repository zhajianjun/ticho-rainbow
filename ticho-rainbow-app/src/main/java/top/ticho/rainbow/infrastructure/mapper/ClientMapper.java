package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.Client;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 客户端信息 mapper
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Repository
public interface ClientMapper extends TiMapper<Client> {

}