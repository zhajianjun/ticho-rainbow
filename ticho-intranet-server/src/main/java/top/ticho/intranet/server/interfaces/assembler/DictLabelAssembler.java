package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.DictLabel;
import top.ticho.intranet.server.interfaces.dto.DictLabelDTO;

/**
 * 字典标签 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper
public interface DictLabelAssembler {
    DictLabelAssembler INSTANCE = Mappers.getMapper(DictLabelAssembler.class);

    /**
     * 字典标签
     *
     * @param dto 字典标签DTO
     * @return {@link DictLabel}
     */
    DictLabel dtoToEntity(DictLabelDTO dto);

    /**
     * 字典标签DTO
     *
     * @param entity 字典标签
     * @return {@link DictLabelDTO}
     */
    DictLabelDTO entityToDto(DictLabel entity);

}