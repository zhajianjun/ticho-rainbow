package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.web.util.valid.ValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "菜单信息DTO")
public class MenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @ApiModelProperty(value = "主键编号", position = 10)
    @NotNull(message = "id不能为空", groups = {ValidGroup.Upd.class})
    @Null(message = "id应为空", groups = {ValidGroup.Add.class})
    private Long id;

    /** 父级id */
    @ApiModelProperty(value = "父级id", position = 20)
    @NotNull(message = "父id不能为空", groups = {ValidGroup.Add.class})
    private Long parentId;

    /** 结构 */
    @ApiModelProperty(value = "结构", position = 30)
    private String structure;

    /** 类型;1-目录,2-菜单,3-权限 */
    @ApiModelProperty(value = "类型;1-目录,2-菜单,3-权限", position = 40)
    @NotNull(message = "类型不能为空", groups = {ValidGroup.Add.class})
    private Integer type;

    /** 权限标识 */
    @ApiModelProperty(value = "权限标识", position = 45)
    // @NotEmpty(message = "权限标识不能为空", groups = {Button.class})
    private List<String> perms;

    /** 标题;目录名称、菜单名称、按钮名称 */
    @ApiModelProperty(value = "标题;目录名称、菜单名称、按钮名称", position = 50)
    @NotBlank(message = "标题不能为空", groups = {ValidGroup.Add.class})
    private String name;

    /** 路由地址 */
    @ApiModelProperty(value = "路由地址", position = 60)
    @NotBlank(message = "路由地址不能为空", groups = {Dir.class, Menu.class})
    private String path;

    /** 组件路径 */
    @ApiModelProperty(value = "组件路径", position = 70)
    @NotBlank(message = "组件路径不能为空", groups = {Ext.class})
    private String component;

    /** 组件名称 */
    @ApiModelProperty(value = "组件名称", position = 75)
    @NotBlank(message = "组件名称不能为空", groups = {Ext.class, Button.class})
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

    /** 当前激活的菜单 */
    @ApiModelProperty(value = "当前激活的菜单", position = 115)
    private String currentActiveMenu;

    /** 菜单是否可关闭;1-是,0-否 */
    @ApiModelProperty(value = "菜单是否可关闭;1-是,0-否", position = 120)
    private Integer closable;

    /** 图标 */
    @ApiModelProperty(value = "图标", position = 130)
    @NotBlank(message = "图标不能为空", groups = {Dir.class})
    private String icon;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 140)
    @NotNull(message = "排序不能为空", groups = {ValidGroup.Add.class})
    private Integer sort;

    /** 状态;1-正常,0-禁用 */
    @ApiModelProperty(value = "状态;1-正常,0-禁用", position = 150)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 160)
    private String remark;


    public interface Dir {
    }

    public interface Menu {
    }

    public interface Button {
    }

    public interface Ext {
    }


}
