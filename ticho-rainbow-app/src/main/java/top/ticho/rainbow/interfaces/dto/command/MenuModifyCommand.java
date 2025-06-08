package top.ticho.rainbow.interfaces.dto.command;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class MenuModifyCommand {

    /** 主键编号 */
    @NotNull(message = "id不能为空")
    private Long id;
    /** 权限标识 */
    private List<String> perms;
    /** 标题;目录名称、菜单名称、按钮名称 */
    @NotBlank(message = "标题不能为空")
    private String name;
    /** 路由地址 */
    private String path;
    /** 组件路径 */
    private String component;
    /** 组件名称 */
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
    private String icon;
    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
