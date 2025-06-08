package top.ticho.rainbow.domain.entity.vo;

/**
 * 菜单信息
 *
 * @param perms             权限标识
 * @param name              标题;目录名称、菜单名称、按钮名称
 * @param path              路由地址
 * @param component         组件路径
 * @param componentName     组件名称
 * @param redirect          转发地址
 * @param extFlag           是否外链菜单;1-是,0-否
 * @param keepAlive         是否缓存;1-是,0-否
 * @param invisible         菜单和目录是否可见;1-是,0-否
 * @param currentActiveMenu 当前激活的菜单
 * @param closable          菜单是否可关闭;1-是,0-否
 * @param icon              图标
 * @param sort              排序
 * @param remark            备注信息
 * @param version           版本号
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
public record MenuModifyVO(
    String perms,
    String name,
    String path,
    String component,
    String componentName,
    String redirect,
    Integer extFlag,
    Integer keepAlive,
    Integer invisible,
    String currentActiveMenu,
    Integer closable,
    String icon,
    Integer sort,
    String remark,
    Long version
) {

}