package top.ticho.rainbow.infrastructure.persistence.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.persistence.po.OpLogPO;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 日志信息 mapper
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Repository
public interface OpLogMapper extends TiMapper<OpLogPO> {

}