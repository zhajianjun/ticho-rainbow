package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

/**
 * @author zhajianjun
 * @date 2025-03-02 19:59
 */
@Value
public class DictLabelModifyVO {

    /** 字典编码 */
    String code;
    /** 字典标签 */
    String label;
    /** 字典值 */
    String value;
    /** 图标 */
    String icon;
    /** 颜色 */
    String color;
    /** 排序 */
    Integer sort;
    /** 状态;1-启用,0-停用 */
    Integer status;
    /** 备注信息 */
    String remark;
    /** 版本号 */
    Long version;

}
