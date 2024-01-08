package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.DictType;
import top.ticho.intranet.server.interfaces.query.DictTypeQuery;

import java.util.List;

/**
 * 数据字典类型 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictTypeRepository extends RootService<DictType> {

    /**
     * 根据条件查询DictType列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link DictType}>
     */
    List<DictType> list(DictTypeQuery query);

}

