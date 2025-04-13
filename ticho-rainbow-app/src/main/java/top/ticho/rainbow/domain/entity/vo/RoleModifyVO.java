package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

/**
 * @author zhajianjun
 * @date 2025-03-08 14:44
 */
@Value
public class RoleModifyVO {

    /** 角色名称 */
    String name;
    /** 状态;1-启用,0-停用 */
    Integer status;
    /** 备注信息 */
    String remark;
    /** 版本号 */
    Long version;

}
