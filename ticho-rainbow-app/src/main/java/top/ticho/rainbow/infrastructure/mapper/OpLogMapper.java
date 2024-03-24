package top.ticho.rainbow.infrastructure.mapper;

import top.ticho.boot.datasource.mapper.RootMapper;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import org.springframework.stereotype.Repository;


/**
 * 日志信息 mapper
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Repository
public interface OpLogMapper extends RootMapper<OpLog> {

}