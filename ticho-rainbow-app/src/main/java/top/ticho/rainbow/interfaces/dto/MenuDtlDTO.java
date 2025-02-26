package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.web.util.TiTreeNode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单功能号详情信息
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "菜单详情信息")
public class MenuDtlDTO extends TiTreeNode<MenuDtlDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 菜单id */
    @ApiModelProperty(value = "菜单id", position = 10)
    private Long id;

    /** 父级id */
    @ApiModelProperty(value = "父级id", position = 20)
    private Long parentId;

    /** 结构 */
    @ApiModelProperty(value = "结构", position = 25)
    private String structure;

    /** 类型;1-目录,2-菜单,3-权限 */
    @ApiModelProperty(value = "类型;1-目录,2-菜单,3-权限", position = 40)
    private Integer type;

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", position = 45)
    private List<String> perms;

    /** 标题;目录名称、菜单名称、按钮名称 */
    @ApiModelProperty(value = "标题;目录名称、菜单名称、按钮名称", position = 50)
    private String name;

    /** 路由地址 */
    @ApiModelProperty(value = "路由地址", position = 60)
    private String path;

    /** 组件路径 */
    @ApiModelProperty(value = "组件路径", position = 70)
    private String component;

    /** 组件名称 */
    @ApiModelProperty(value = "组件名称", position = 75)
    private String componentName;

    /** 转发地址 */
    @ApiModelProperty(value = "转发地址", position = 80)
    private String redirect;

    /** 是否外链菜单;1-是,0-否 */
    @ApiModelProperty(value = "是否外链菜单;1-是,0-否", position = 90)
    private Integer extFlag;

    /** 是否缓存;1-是,0-否 */
    @ApiModelProperty(value = "是否缓存;1-是,0-否", position = 100)
    private Integer keepAlive;

    /** 菜单和目录是否可见;1-是,0-否 */
    @ApiModelProperty(value = "菜单和目录是否可见;1-是,0-否", position = 110)
    private Integer invisible;

    /** 菜单是否可关闭;1-是,0-否 */
    @ApiModelProperty(value = "菜单是否可关闭;1-是,0-否", position = 120)
    private Integer closable;

    /** 图标 */
    @ApiModelProperty(value = "图标", position = 130)
    private String icon;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 140)
    private Integer sort;

    /** 状态;1-正常,0-禁用 */
    @ApiModelProperty(value = "状态;1-正常,0-禁用", position = 150)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 160)
    private String remark;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 170)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 是否选中 */
    @ApiModelProperty(value = "是否选中;true-选中,false-未选中", position = 180)
    private Boolean checkbox;

}
