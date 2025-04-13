package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.response.DictDTO;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.infrastructure.persistence.po.DictPO;

import java.util.List;

/**
 * 字典 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:16
 */
@Mapper(componentModel = "spring")
public interface DictConverter {

    List<Dict> toEntitys(List<DictPO> list);

    Dict toEntity(DictPO dictPO);

    DictPO toPO(Dict dict);

    DictDTO toDTO(DictPO dictPO);

}
