package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

/**
 * 菜单信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Value
public class MenuModifyVO {

    /** 权限标识 */
    String perms;
    /** 标题;目录名称、菜单名称、按钮名称 */
    String name;
    /** 路由地址 */
    String path;
    /** 组件路径 */
    String component;
    /** 组件名称 */
    String componentName;
    /** 转发地址 */
    String redirect;
    /** 是否外链菜单;1-是,0-否 */
    Integer extFlag;
    /** 是否缓存;1-是,0-否 */
    Integer keepAlive;
    /** 菜单和目录是否可见;1-是,0-否 */
    Integer invisible;
    /** 当前激活的菜单 */
    String currentActiveMenu;
    /** 菜单是否可关闭;1-是,0-否 */
    Integer closable;
    /** 图标 */
    String icon;
    /** 排序 */
    Integer sort;
    /** 状态;1-启用,0-停用 */
    Integer status;
    /** 备注信息 */
    String remark;

}