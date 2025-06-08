package top.ticho.rainbow.domain.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.DictModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.rainbow.infrastructure.common.enums.YesOrNo;
import top.ticho.starter.view.util.TiAssert;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Getter
@Builder
public class Dict implements Entity {

    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典名称 */
    private String name;
    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;
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

    public void modify(DictModifyVO dictModifyVO) {
        this.name = dictModifyVO.name();
        this.isSys = dictModifyVO.isSys();
        this.remark = dictModifyVO.remark();
        this.version = dictModifyVO.version();
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()),
            StrUtil.format("只有[{}]状态才能执行启用操作，字典：{}", disable.message(), name));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()),
            StrUtil.format("只有[{}]状态才能执行禁用操作，字典：{}", enable.message(), name));
        TiAssert.isTrue(!Objects.equals(this.isSys, YesOrNo.YES.code()),
            StrUtil.format("系统字典无法禁用，字典：{}", name));
        this.status = CommonStatus.DISABLE.code();
    }


}