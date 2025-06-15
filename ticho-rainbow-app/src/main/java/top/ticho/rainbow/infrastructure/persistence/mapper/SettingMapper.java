package top.ticho.rainbow.infrastructure.persistence.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.persistence.po.SettingPO;
import top.ticho.starter.datasource.mapper.TiMapper;

/**
 * 配置信息 mapper
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Repository
public interface SettingMapper extends TiMapper<SettingPO> {

}