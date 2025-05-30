package top.ticho.rainbow.infrastructure.persistence.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.persistence.po.DictLabelPO;
import top.ticho.starter.datasource.mapper.TiMapper;

/**
 * 字典标签 mapper
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Repository
public interface DictLabelMapper extends TiMapper<DictLabelPO> {

}