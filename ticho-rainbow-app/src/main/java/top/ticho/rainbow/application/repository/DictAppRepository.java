package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.application.dto.query.DictQuery;
import top.ticho.rainbow.application.dto.response.DictDTO;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-13 17:59
 */
public interface DictAppRepository {

    /**
     * 根据条件查询DictType列表(分页)
     *
     * @param query 查询
     * @return {@link TiPageResult }<{@link DictDTO }>
     */
    TiPageResult<DictDTO> page(DictQuery query);

}
