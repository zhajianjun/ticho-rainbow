package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.excel.SettingExcelExport;
import top.ticho.rainbow.domain.entity.Setting;
import top.ticho.rainbow.domain.entity.vo.SettingModifyVO;
import top.ticho.rainbow.interfaces.command.SettingModifyCommand;
import top.ticho.rainbow.interfaces.command.SettingSaveCommand;
import top.ticho.rainbow.interfaces.dto.SettingDTO;
import top.ticho.starter.web.util.TiIdUtil;


/**
 * 配置信息 转换
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class})
public interface SettingAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    Setting toEntity(SettingSaveCommand settingSaveCommand);

    SettingModifyVO toModifyfVO(SettingModifyCommand settingModifyCommand);

    SettingExcelExport toExcelExport(SettingDTO settingDTO);

}