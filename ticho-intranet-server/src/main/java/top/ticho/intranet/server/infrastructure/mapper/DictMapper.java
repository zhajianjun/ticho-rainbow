package top.ticho.intranet.server.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.boot.datasource.mapper.RootMapper;
import top.ticho.intranet.server.infrastructure.entity.Dict;

/**
 * 字典 mapper
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Repository
public interface DictMapper extends RootMapper<Dict> {

}