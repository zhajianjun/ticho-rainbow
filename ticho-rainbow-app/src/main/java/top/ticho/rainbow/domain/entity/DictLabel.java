package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.DictLabelModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.tool.core.TiAssert;
import top.ticho.tool.core.TiStrUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典标签
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Getter
@Builder
public class DictLabel implements Entity {

    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典标签 */
    private String label;
    /** 字典值 */
    private String value;
    /** 图标 */
    private String icon;
    /** 颜色 */
    private String color;
    /** 排序 */
    private Integer sort;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;

    public void modify(DictLabelModifyVO dictLabelModifyVO, boolean isSysDict) {
        if (!isSysDict) {
            this.code = dictLabelModifyVO.code();
            this.label = dictLabelModifyVO.label();
            this.value = dictLabelModifyVO.value();
        }
        this.icon = dictLabelModifyVO.icon();
        this.color = dictLabelModifyVO.color();
        this.sort = dictLabelModifyVO.sort();
        this.remark = dictLabelModifyVO.remark();
        this.version = dictLabelModifyVO.version();
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()),
            TiStrUtil.format("只有[{}]状态才能执行启用操作，字典标签：{}", disable.message(), label));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()),
            TiStrUtil.format("只有[{}]状态才能执行禁用操作，字典标签：{}", enable.message(), label));
        this.status = CommonStatus.DISABLE.code();
    }

}