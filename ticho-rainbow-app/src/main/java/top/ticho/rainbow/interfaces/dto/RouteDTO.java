package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.web.util.TiTreeNode;

import java.io.Serializable;
import java.util.List;

/**
 * 路由菜单DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "路由菜单DTO")
public class RouteDTO extends TiTreeNode<RouteDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 菜单id */
    @ApiModelProperty(value = "菜单id", position = 10)
    private Long id;

    /** 父级id */
    @ApiModelProperty(value = "父级id", position = 20)
    private Long parentId;

    /** 类型;1-目录,2-菜单,3-权限 */
    @ApiModelProperty(value = "类型;1-目录,2-菜单,3-权限", position = 40)
    private Integer type;

    /** 标题;目录名称、菜单名称、按钮名称 */
    @ApiModelProperty(value = "标题;目录名称、菜单名称、按钮名称", position = 50)
    private String name;

    @ApiModelProperty(value = "路由元数据", position = 55)
    private RouteMetaDTO meta;

    /** 路由地址 */
    @ApiModelProperty(value = "路由地址", position = 60)
    private String path;

    /** 组件路径 */
    @ApiModelProperty(value = "组件路径", position = 70)
    private String component;

    /** 转发地址 */
    @ApiModelProperty(value = "转发地址", position = 80)
    private String redirect;

    /** 权限标识 */
    @ApiModelProperty(value = "按钮名称", position = 90)
    private List<String> buttons;

}
