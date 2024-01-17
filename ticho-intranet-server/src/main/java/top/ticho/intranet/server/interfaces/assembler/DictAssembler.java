package top.ticho.intranet.server.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.interfaces.dto.DictDTO;

/**
 * 字典 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper
public interface DictAssembler {
    DictAssembler INSTANCE = Mappers.getMapper(DictAssembler.class);

    /**
     * 字典
     *
     * @param dto 字典DTO
     * @return {@link Dict}
     */
    Dict dtoToEntity(DictDTO dto);

    /**
     * 字典DTO
     *
     * @param entity 字典
     * @return {@link DictDTO}
     */
    DictDTO entityToDto(Dict entity);

}