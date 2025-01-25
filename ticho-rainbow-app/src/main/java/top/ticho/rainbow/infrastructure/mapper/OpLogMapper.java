package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 日志信息 mapper
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Repository
public interface OpLogMapper extends TiMapper<OpLog> {

}