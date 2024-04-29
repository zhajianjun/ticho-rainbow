package top.ticho.rainbow.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
public class Menu extends Model<Menu> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    /** 状态;1-正常,0-禁用 */
    private Integer status;

    /** 备注信息 */
    private String remark;

    /** 乐观锁;控制版本更改 */
    private Long version;

    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 删除标识;0-未删除,1-已删除 */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;

}