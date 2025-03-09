package top.ticho.rainbow.application.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 路由元数据
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data

@ApiModel(value = "日志信息DTO")
public class RouteMetaDTO {


    /** 菜单排序 */
    private Integer orderNo;

    /** 路由title  一般必填 */
    private String title;

    /** 动态路由可打开Tab页数 */
    private Integer dynamicLevel;

    /** 动态路由的实际Path, 即去除路由的动态部分; */
    private String realPath;

    /** Whether to ignore permissions */
    private Boolean ignoreAuth;

    /** 是否忽略KeepAlive缓存 */
    private Boolean ignoreKeepAlive;

    /** 是否固定标签 */
    private Boolean affix;

    /** 图标，也是菜单图标 */
    private String icon;

    /** 内嵌iframe的地址 */
    private String frameSrc;

    /** 指定该路由切换的动画名 */
    private String transitionName;

    /** 隐藏该路由在面包屑上面的显示 */
    private Boolean hideBreadcrumb;

    /** 隐藏所有子菜单 */
    private Boolean hideChildrenInMenu;

    /** 如果该路由会携带参数，且需要在tab页上面显示。则需要设置为true */
    private Boolean carryParam;

    /** Used internally to mark single-level menus */
    private Boolean single;

    /** 当前激活的菜单。用于配置详情页时左侧激活的菜单路径 */
    private String currentActiveMenu;

    /** 当前路由不再标签页显示 */
    private Boolean hideTab;

    /** 当前路由不再菜单显示 */
    private Boolean hideMenu;

    private Boolean isLink;

    /** 忽略路由。用于在ROUTE_MAPPING以及BACK权限模式下，生成对应的菜单而忽略路由。2.5.3以上版本有效 */
    private Boolean ignoreRoute;

    /** 是否在子级菜单的完整path中忽略本级path */
    private Boolean hidePathForChildren;


}
