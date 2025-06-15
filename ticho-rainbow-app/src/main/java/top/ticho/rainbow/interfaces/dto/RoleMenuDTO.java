package top.ticho.rainbow.interfaces.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户角色菜单功能号详情
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
public class RoleMenuDTO {

    /** 角色id列表 */
    private List<Long> roleIds;
    /** 角色code列表 */
    private List<String> roleCodes;
    /** 菜单id列表 */
    private List<Long> menuIds;
    /** 权限标识 */
    private List<String> perms;
    /** 角色信息 */
    private List<RoleDTO> roles;
    /** 菜单信息 */
    private List<MenuDTO> menus;

}
