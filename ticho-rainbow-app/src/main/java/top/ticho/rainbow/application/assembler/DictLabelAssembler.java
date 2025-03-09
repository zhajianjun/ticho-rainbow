package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.command.DictLabelModifyCommand;
import top.ticho.rainbow.application.dto.command.DictLabelSaveCommand;
import top.ticho.rainbow.application.dto.excel.DictExp;
import top.ticho.rainbow.application.dto.response.DictLabelDTO;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.vo.DictLabelModifyVO;

/**
 * 字典标签 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring")
public interface DictLabelAssembler {

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    DictLabel toEntity(DictLabelSaveCommand dictLabelSaveCommand);

    DictLabelModifyVO toVO(DictLabelModifyCommand dictLabelModifyCommand);

    DictLabelDTO toDTO(DictLabel dictLabel);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "statusName", ignore = true)
    @Mapping(target = "name", ignore = true)
    DictExp toExport(DictLabel dictLabel);

}