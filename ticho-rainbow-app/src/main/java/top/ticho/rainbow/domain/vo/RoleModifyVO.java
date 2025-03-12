package top.ticho.rainbow.domain.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * @author zhajianjun
 * @date 2025-03-08 14:44
 */
@Getter
@Builder
public class RoleModifyVO {

    /** 角色名称 */
    private String name;    /** 状态;1-正常,0-禁用 */
    private Integer status;    /** 备注信息 */
    private String remark;    /** 版本号 */
    private Long version;
}
