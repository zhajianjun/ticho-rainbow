package top.ticho.rainbow.application.dto;

import lombok.Data;
import top.ticho.starter.web.util.valid.TiValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * 菜单信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class MenuDTO {

    /** 主键编号 */
    @NotNull(message = "id不能为空", groups = {TiValidGroup.Upd.class})
    @Null(message = "id应为空", groups = {TiValidGroup.Add.class})
    private Long id;
    /** 父级id */
    @NotNull(message = "父id不能为空", groups = {TiValidGroup.Add.class})
    private Long parentId;
    /** 结构 */
    private String structure;
    /** 类型;1-目录,2-菜单,3-权限 */
    @NotNull(message = "类型不能为空", groups = {TiValidGroup.Add.class})
    private Integer type;
    /** 权限标识 */
    // @NotEmpty(message = "权限标识不能为空", groups = {Button.class})
    private List<String> perms;
    /** 标题;目录名称、菜单名称、按钮名称 */
    @NotBlank(message = "标题不能为空", groups = {TiValidGroup.Add.class})
    private String name;
    /** 路由地址 */
    @NotBlank(message = "路由地址不能为空", groups = {Dir.class, Menu.class})
    private String path;
    /** 组件路径 */
    @NotBlank(message = "组件路径不能为空", groups = {Ext.class})
    private String component;
    /** 组件名称 */
    @NotBlank(message = "组件名称不能为空", groups = {Ext.class, Button.class})
    private String componentName;
    /** 转发地址 */
    private String redirect;
    /** 是否外链菜单;1-是,0-否 */
    private Integer extFlag;
    /** 是否缓存;1-是,0-否 */
    private Integer keepAlive;
    /** 菜单和目录是否可见;1-是,0-否 */
    private Integer invisible;
    /** 当前激活的菜单 */
    private String currentActiveMenu;
    /** 菜单是否可关闭;1-是,0-否 */
    private Integer closable;
    /** 图标 */
    @NotBlank(message = "图标不能为空", groups = {Dir.class})
    private String icon;
    /** 排序 */
    @NotNull(message = "排序不能为空", groups = {TiValidGroup.Add.class})
    private Integer sort;
    /** 状态;1-正常,0-禁用 */
    private Integer status;
    /** 备注信息 */
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
