package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * 角色菜单关联关系
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Getter
@Builder
public class RoleMenu {

    /** 角色id */
    private Long roleId;
    /** 菜单id */
    private Long menuId;
}