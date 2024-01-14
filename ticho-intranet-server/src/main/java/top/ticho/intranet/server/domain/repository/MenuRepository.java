package top.ticho.intranet.server.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.intranet.server.infrastructure.entity.Menu;
import top.ticho.intranet.server.interfaces.query.MenuQuery;

import java.util.List;

/**
 * 菜单信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface MenuRepository extends RootService<Menu> {

    /**
     * 根据条件查询Menu列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> list(MenuQuery query);

    /**
     * 统计子节点某些类型和路由地址的数量
     *
     * @param types     类型
     * @param path      路由地址
     * @param excludeId 排除的编号
     * @return long
     */
    Menu getByTypesAndPath(List<Integer> types, String path, Long excludeId);

    /**
     * 获取组件名称的数量
     *
     * @param types         类型
     * @param componentName 组件名称
     * @param excludeId     排除的编号
     * @return long
     */
    Menu getByTypesAndComNameExcludeId(List<Integer> types, String componentName, Long excludeId);

}

