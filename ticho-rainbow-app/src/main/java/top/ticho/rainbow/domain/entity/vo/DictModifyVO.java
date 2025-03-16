package top.ticho.rainbow.domain.entity.vo;

import lombok.Value;

/**
 * @author zhajianjun
 * @date 2025-03-02 19:59
 */
@Value
public class DictModifyVO {

    String name;
    /** 是否系统字典;1-是,0-否 */
    Integer isSys;
    /** 状态;1-启用,0-停用 */
    Integer status;
    /** 备注信息 */
    String remark;
    /** 版本号 */
    Long version;

}
