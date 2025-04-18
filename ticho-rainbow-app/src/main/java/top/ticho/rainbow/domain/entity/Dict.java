package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.DictModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.YesOrNo;

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
public class Dict {

    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典名称 */
    private String name;
    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;
    /** 状态;1-启用,0-停用 */
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
        this.status = dictModifyVO.status();
        this.remark = dictModifyVO.remark();
        this.version = dictModifyVO.version();
        if (Objects.equals(YesOrNo.YES.code(), dictModifyVO.isSys())) {
            this.status = 1;
        }
    }

}