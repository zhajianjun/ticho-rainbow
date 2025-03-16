package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.DictLabelModifyVO;

import java.time.LocalDateTime;

/**
 * 字典标签
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Getter
@Builder
public class DictLabel {

    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典标签 */
    private String label;
    /** 字典值 */
    private String value;
    /** 图标 */
    private String icon;
    /** 颜色 */
    private String color;
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

    public void modify(DictLabelModifyVO dictLabelModifyVO, boolean isSysDict) {
        this.code = dictLabelModifyVO.getCode();
        this.label = dictLabelModifyVO.getLabel();
        this.value = dictLabelModifyVO.getValue();
        this.icon = dictLabelModifyVO.getIcon();
        this.color = dictLabelModifyVO.getColor();
        this.sort = dictLabelModifyVO.getSort();
        this.status = dictLabelModifyVO.getStatus();
        this.remark = dictLabelModifyVO.getRemark();
        if (isSysDict) {
            this.code = null;
            this.label = null;
            this.value = null;
            this.status = 1;
        }
    }

}