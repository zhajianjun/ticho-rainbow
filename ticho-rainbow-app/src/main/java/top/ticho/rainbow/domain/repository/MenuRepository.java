package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.application.dto.query.MenuQuery;
import top.ticho.rainbow.domain.entity.Menu;

import java.util.List;

/**
 * 菜单信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface MenuRepository {
    /**
     * 保存菜单
     *
     * @param menu 菜单
     * @return boolean
     */
    boolean save(Menu menu);

    /**
     * 删除菜单
     *
     * @param id 编号
     * @return boolean
     */
    boolean remove(Long id);

    /**
     * 修改菜单
     *
     * @param menu 菜单
     * @return boolean
     */
    boolean modify(Menu menu);

    /**
     * 根据编号查询菜单
     *
     * @param id 编号
     * @return {@link Menu }
     */
    Menu find(Long id);

    /**
     * 查询菜单
     *
     * @return {@link List}<{@link Menu}>
     */
    List<Menu> cacheList();

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

