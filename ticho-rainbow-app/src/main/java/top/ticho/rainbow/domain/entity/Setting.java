package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.SettingModifyVO;

import java.time.LocalDateTime;

/**
 * 配置信息
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Getter
@Builder
public class Setting implements Entity {

    /** 主键编号 */
    private Long id;
    /** key */
    private String key;
    /** value */
    private String value;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;
    /** 删除标识;0-未删除,1-已删除 */
    private Integer isDelete;

    public void modify(SettingModifyVO settingModifyVO) {
        this.key = settingModifyVO.getKey();
        this.value = settingModifyVO.getValue();
        this.sort = settingModifyVO.getSort();
        this.remark = settingModifyVO.getRemark();
    }

}