package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.interfaces.query.DictQuery;

import java.util.List;

/**
 * 数据字典 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface DictRepository extends RootService<Dict> {

    /**
     * 根据条件查询Dict列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Dict}>
     */
    List<Dict> list(DictQuery query);

    /**
     * 根据类型id查询是否存在
     *
     * @param typeId id类型
     * @return boolean
     */
    boolean existsByTypeId(Long typeId);

    /**
     * 根据编号查询所有子孙节点的id
     *
     * @param id 主键id
     *
     * @return {@link List}<{@link Integer}>
     */
    List<Long> getDescendantIds(Long id);


    /**
     * 根据id查询兄弟节点，包括自己
     *
     * @param id  主键id
     * @return {@link List}<{@link Dict}>
     */
    List<Dict> getBrothers(Long id);
}

