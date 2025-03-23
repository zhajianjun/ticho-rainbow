package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.command.DictModifyCommand;
import top.ticho.rainbow.application.dto.command.DictSaveCommand;
import top.ticho.rainbow.application.dto.response.DictDTO;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.entity.vo.DictModifyVO;
import top.ticho.rainbow.infrastructure.core.enums.YesOrNo;
import top.ticho.starter.web.util.TiIdUtil;

import java.util.Objects;

/**
 * 字典 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring", imports = {Objects.class, YesOrNo.class, TiIdUtil.class})
public interface DictAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    // 系统字典默认为正常
    @Mapping(target = "status", expression = "java(Objects.equals(YesOrNo.YES.code(), dto.getIsSys()) ?  1 : dto.getStatus())")
    Dict toEntity(DictSaveCommand dto);

    DictModifyVO toVO(DictModifyCommand dictModifyCommand);

    @Mapping(target = "details", ignore = true)
    DictDTO toDTO(Dict entity);


}