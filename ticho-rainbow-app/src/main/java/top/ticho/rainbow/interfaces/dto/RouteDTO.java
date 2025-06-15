package top.ticho.rainbow.interfaces.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.web.util.TiTreeNode;

import java.util.List;

/**
 * 路由菜单DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RouteDTO extends TiTreeNode<RouteDTO> {

    /** 菜单id */
    private Long id;
    /** 父级id */
    private Long parentId;
    /** 类型;1-目录,2-菜单,3-权限 */
    private Integer type;
    /** 标题;目录名称、菜单名称、按钮名称 */
    private String name;
    private RouteMetaDTO meta;
    /** 路由地址 */
    private String path;
    /** 组件路径 */
    private String component;
    /** 转发地址 */
    private String redirect;
    /** 权限标识 */
    private List<String> buttons;

}
