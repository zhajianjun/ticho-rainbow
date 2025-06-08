package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.interfaces.dto.command.DictLabelModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.DictLabelSaveCommand;
import top.ticho.rainbow.application.dto.excel.DictExcelExport;
import top.ticho.rainbow.interfaces.dto.response.DictLabelDTO;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.entity.vo.DictLabelModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.web.util.TiIdUtil;

/**
 * 字典标签 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring", imports = {TiIdUtil.class, CommonStatus.class})
public interface DictLabelAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "status", expression = "java(CommonStatus.DISABLE.code())")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    DictLabel toEntity(DictLabelSaveCommand dictLabelSaveCommand);

    DictLabelModifyVO toVO(DictLabelModifyCommand dictLabelModifyCommand);

    DictLabelDTO toDTO(DictLabel dictLabel);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "statusName", ignore = true)
    @Mapping(target = "name", ignore = true)
    DictExcelExport toExcelExportort(DictLabel dictLabel);

}