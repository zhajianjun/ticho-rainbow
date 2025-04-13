package top.ticho.rainbow.application.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.util.List;

/**
 * 角色信息查询条件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 主键编号 */
    private Long id;
    /** 角色编码 */
    private String code;
    /** 角色名称 */
    private String name;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 备注信息 */
    private String remark;

}
