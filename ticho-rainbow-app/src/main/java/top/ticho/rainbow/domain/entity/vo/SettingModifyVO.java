package top.ticho.rainbow.domain.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配置信息VO
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Getter
@AllArgsConstructor
public class SettingModifyVO {

    /** value */
    private String value;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;

}