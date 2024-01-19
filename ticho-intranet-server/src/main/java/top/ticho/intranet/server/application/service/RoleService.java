package top.ticho.intranet.server.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.intranet.server.interfaces.dto.RoleDTO;
import top.ticho.intranet.server.interfaces.dto.RoleMenuDTO;
import top.ticho.intranet.server.interfaces.dto.RoleMenuDtlDTO;
import top.ticho.intranet.server.interfaces.query.RoleDtlQuery;
import top.ticho.intranet.server.interfaces.query.RoleQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息 服务接口
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public interface RoleService {
    /**
     * 保存角色信息
     *
     * @param roleDTO 角色信息DTO 对象
     */
    void save(RoleDTO roleDTO);

    /**
     * 删除角色信息
     *
     * @param id 主键
     */
    void removeById(Long id);

    /**
     * 修改角色信息
     *
     * @param roleDTO 角色信息DTO 对象
     */
    void updateById(RoleDTO roleDTO);

    /**
     * 更新状态
     *
     * @param id     id
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据id查询角色信息
     *
     * @param id 主键
     * @return {@link RoleDTO}
     */
    RoleDTO getById(Serializable id);

    /**
     * 分页查询角色信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link RoleDTO}>
     */
    PageResult<RoleDTO> page(RoleQuery query);

    /**
     * 查询角色信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link RoleDTO}>
     */
    List<RoleDTO> list(RoleQuery query);

    /**
     * 绑定菜单
     *
     * @param roleMenuDTO 用户角色dto
     */
    void bindMenu(RoleMenuDTO roleMenuDTO);

    /**
     * 角色菜单详情查询
     *
     * @param roleDtlQuery 迪泰查询角色
     * @return {@link RoleMenuDtlDTO}
     */
    RoleMenuDtlDTO listRoleMenu(RoleDtlQuery roleDtlQuery);
}

