package top.ticho.rainbow.domain.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * @author zhajianjun
 * @date 2025-03-02 19:59
 */
@Getter
@Builder
public class DictModifyVO {

    private String name;    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;    /** 状态;1-启用,0-停用 */
    private Integer status;    /** 备注信息 */
    private String remark;    /** 版本号 */
    private Long version;
}
