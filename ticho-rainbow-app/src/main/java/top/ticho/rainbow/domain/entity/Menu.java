package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.MenuModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.tool.core.TiStrUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 菜单信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Getter
@Builder
public class Menu implements Entity {

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
    /** 状态;1-启用,0-禁用 */
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
        this.perms = menuModifyVO.perms();
        this.name = menuModifyVO.name();
        this.path = menuModifyVO.path();
        this.component = menuModifyVO.component();
        this.componentName = menuModifyVO.componentName();
        this.redirect = menuModifyVO.redirect();
        this.extFlag = menuModifyVO.extFlag();
        this.keepAlive = menuModifyVO.keepAlive();
        this.invisible = menuModifyVO.invisible();
        this.currentActiveMenu = menuModifyVO.currentActiveMenu();
        this.closable = menuModifyVO.closable();
        this.icon = menuModifyVO.icon();
        this.sort = menuModifyVO.sort();
        this.remark = menuModifyVO.remark();
        this.version = menuModifyVO.version();
    }

    public boolean isEnable() {
        return Objects.equals(CommonStatus.ENABLE.code(), status);
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()),
            TiStrUtil.format("只有[{}]状态才能执行启用操作，菜单：{}", disable.message(), name));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()),
            TiStrUtil.format("只有[{}]状态才能执行禁用操作，菜单：{}", enable.message(), name));
        this.status = CommonStatus.DISABLE.code();
    }

}