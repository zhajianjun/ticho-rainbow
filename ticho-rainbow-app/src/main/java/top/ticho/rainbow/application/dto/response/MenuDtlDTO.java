package top.ticho.rainbow.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.web.util.TiTreeNode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单详情信息
 * 菜单功能号详情信息
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuDtlDTO extends TiTreeNode<MenuDtlDTO> {

    /** 菜单id */
    private Long id;
    /** 父级id */
    private Long parentId;
    /** 结构 */
    private String structure;
    /** 类型;1-目录,2-菜单,3-权限 */
    private Integer type;
    /** 权限标识 */
    private List<String> perms;
    /** 标题;目录名称、菜单名称、按钮名称 */
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
    /** 菜单是否可关闭;1-是,0-否 */
    private Integer closable;
    /** 图标 */
    private String icon;
    /** 排序 */
    private Integer sort;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 是否选中 */
    private Boolean checkbox;

}
