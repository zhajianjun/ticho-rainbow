package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Menu;

import java.util.List;

/**
 * 菜单信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface MenuRepository {

    boolean save(Menu menu);

    boolean remove(Long id);

    boolean modify(Menu menu);

    boolean modifyBatch(List<Menu> menus);

    Menu find(Long id);

    List<Menu> list(List<Long> ids);

    List<Menu> cacheList();

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

