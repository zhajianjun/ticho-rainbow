package top.ticho.rainbow.domain.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * @author zhajianjun
 * @date 2025-03-02 19:59
 */
@Getter
@Builder
public class DictLabelModifyVO {
    /** 字典编码 */
    private String code;    /** 字典标签 */
    private String label;    /** 字典值 */
    private String value;    /** 图标 */
    private String icon;    /** 颜色 */
    private String color;    /** 排序 */
    private Integer sort;    /** 状态;1-启用,0-停用 */
    private Integer status;    /** 备注信息 */
    private String remark;    /** 版本号 */
    private Long version;}
