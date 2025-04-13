package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.MenuModifyVO;

import java.time.LocalDateTime;

/**
 * 菜单信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Getter
@Builder
public class Menu {

    /** 主键编号 */
    private Long id;
    /** 父级id */
    private Long parentId;
    /** 结构 */
    private String structure;
    /** 类型;1-目录,2-菜单,3-权限 */
    private Integer type;
    /** 权限标识 */
    private String perms;
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
    /** 当前激活的菜单 */
    private String currentActiveMenu;
    /** 菜单是否可关闭;1-是,0-否 */
    private Integer closable;
    /** 图标 */
    private String icon;
    /** 排序 */
    private Integer sort;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;

    public void modify(Long parentId, String structure) {
        this.parentId = parentId;
        this.structure = structure;
    }

    public void modifyCurrentActiveMenu(String currentActiveMenu) {
        this.currentActiveMenu = currentActiveMenu;
    }

    public void modify(MenuModifyVO menuModifyVO) {
        this.perms = menuModifyVO.getPerms();
        this.name = menuModifyVO.getName();
        this.path = menuModifyVO.getPath();
        this.component = menuModifyVO.getComponent();
        this.componentName = menuModifyVO.getComponentName();
        this.redirect = menuModifyVO.getRedirect();
        this.extFlag = menuModifyVO.getExtFlag();
        this.keepAlive = menuModifyVO.getKeepAlive();
        this.invisible = menuModifyVO.getInvisible();
        this.currentActiveMenu = menuModifyVO.getCurrentActiveMenu();
        this.closable = menuModifyVO.getClosable();
        this.icon = menuModifyVO.getIcon();
        this.sort = menuModifyVO.getSort();
        this.status = menuModifyVO.getStatus();
        this.remark = menuModifyVO.getRemark();
    }

}