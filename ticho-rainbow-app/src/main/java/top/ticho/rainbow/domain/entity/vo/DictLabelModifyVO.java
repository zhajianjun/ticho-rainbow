package top.ticho.rainbow.domain.entity.vo;

/**
 * @param code    字典编码
 * @param label   字典标签
 * @param value   字典值
 * @param icon    图标
 * @param color   颜色
 * @param sort    排序
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
    String remark,
    Long version
) {

}
