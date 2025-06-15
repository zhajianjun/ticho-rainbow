package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.SettingDTO;
import top.ticho.rainbow.interfaces.query.SettingQuery;
import top.ticho.starter.view.core.TiPageResult;

/**
 * 配置信息 appRepository接口
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
public interface SettingAppRepository {

    TiPageResult<SettingDTO> page(SettingQuery settingQuery);

}

