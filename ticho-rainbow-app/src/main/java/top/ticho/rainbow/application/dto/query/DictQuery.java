package top.ticho.rainbow.application.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.util.List;

/**
 * 字典查询条件
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典名称 */
    private String name;
    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 备注信息 */
    private String remark;

}
