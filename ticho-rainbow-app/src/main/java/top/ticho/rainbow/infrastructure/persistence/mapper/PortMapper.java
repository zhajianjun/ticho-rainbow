package top.ticho.rainbow.infrastructure.persistence.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.persistence.po.PortPO;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 端口信息 mapper
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Repository
public interface PortMapper extends TiMapper<PortPO> {

}