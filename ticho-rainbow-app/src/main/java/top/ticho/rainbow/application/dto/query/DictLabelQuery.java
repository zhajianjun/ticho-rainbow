package top.ticho.rainbow.application.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

/**
 * 字典标签查询条件
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictLabelQuery extends TiPageQuery {

    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典标签 */
    private String label;
    /** 字典值 */
    private String value;
    /** 排序 */
    private Integer sort;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 备注信息 */
    private String remark;

}
