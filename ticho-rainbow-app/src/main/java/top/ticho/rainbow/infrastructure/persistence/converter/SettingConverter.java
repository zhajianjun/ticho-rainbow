package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.Setting;
import top.ticho.rainbow.infrastructure.persistence.po.SettingPO;
import top.ticho.rainbow.interfaces.dto.SettingDTO;

import java.util.List;

/**
 * 配置信息 转换
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Mapper(componentModel = "spring")
public interface SettingConverter {

    Setting toEntity(SettingPO settingPO);

    SettingDTO toDTO(SettingPO settingPO);

    List<Setting> toEntity(List<SettingPO> settingPOs);

    SettingPO toPO(Setting setting);

    List<SettingPO> toPO(List<Setting> settings);

}