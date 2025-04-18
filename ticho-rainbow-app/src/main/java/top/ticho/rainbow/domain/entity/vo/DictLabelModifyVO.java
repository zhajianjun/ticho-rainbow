package top.ticho.rainbow.domain.entity.vo;

/**
 * @param code    字典编码
 * @param label   字典标签
 * @param value   字典值
 * @param icon    图标
 * @param color   颜色
 * @param sort    排序
 * @param status  状态;1-启用,0-停用
 * @param remark  备注信息
 * @param version 版本号
 * @author zhajianjun
 * @date 2025-03-02 19:59
 */
public record DictLabelModifyVO(
    String code,
    String label,
    String value,
    String icon,
    String color,
    Integer sort,
    Integer status,
    String remark,
    Long version
) {

}
