package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.DictType;
import top.ticho.intranet.server.interfaces.dto.DictTypeDTO;

/**
 * 数据字典类型 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper
public interface DictTypeAssembler {
    DictTypeAssembler INSTANCE = Mappers.getMapper(DictTypeAssembler.class);

    /**
     * 数据字典类型
     *
     * @param dto 数据字典类型DTO
     * @return {@link DictType}
     */
    DictType dtoToEntity(DictTypeDTO dto);

    /**
     * 数据字典类型DTO
     *
     * @param entity 数据字典类型
     * @return {@link DictTypeDTO}
     */
    DictTypeDTO entityToDto(DictType entity);

}