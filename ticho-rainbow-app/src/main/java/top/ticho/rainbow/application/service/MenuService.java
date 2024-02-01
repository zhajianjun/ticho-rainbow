package top.ticho.rainbow.application.service;


import top.ticho.rainbow.interfaces.dto.MenuDTO;
import top.ticho.rainbow.interfaces.dto.MenuDtlDTO;
import top.ticho.rainbow.interfaces.dto.RouteDTO;

import java.util.List;

/**
 * 菜单信息 服务接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface MenuService {
    /**
     * 保存菜单信息
     *
     * @param menuDTO 菜单信息DTO 对象
     */
    void save(MenuDTO menuDTO);

    /**
     * 删除菜单信息
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改菜单信息
     *
     * @param menuDTO 菜单信息DTO 对象
     */
    void updateById(MenuDTO menuDTO);

    /**
     * 根据id查询菜单信息
     *
     * @param id 主键
     * @return {@link MenuDTO}
     */
    MenuDTO getById(Long id);

    /**
     * 获取所有菜单信息
     *
     * @return {@link List}<{@link MenuDtlDTO}>
     */
    List<MenuDtlDTO> list();

    /**
     * 查询所有菜单路由信息
     *
     * @return {@link List}<{@link RouteDTO}>
     */
    List<RouteDTO> route();

    /**
     * 查询权限编码
     *
     * @return {@link List}<{@link RouteDTO}>
     */
    List<String> getPerms(List<String> roleCodes);

}

