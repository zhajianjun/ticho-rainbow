package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.infrastructure.persistence.po.DictLabelPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:15
 */
@Mapper(componentModel = "spring")
public interface DictLabelConverter {

    List<DictLabel> toEntity(List<DictLabelPO> list);

    DictLabel toEntity(DictLabelPO dictLabelPO);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    DictLabelPO toPo(DictLabel dictLabel);

    List<DictLabelPO> toPo(List<DictLabel> dictLabels);

}
