package top.ticho.rainbow.interfaces.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.util.List;

/**
 * 配置信息Query
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SettingQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** key */
    private String key;
    /** value */
    private String value;
    /** 备注信息 */
    private String remark;

}