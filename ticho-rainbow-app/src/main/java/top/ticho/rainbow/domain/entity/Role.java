package top.ticho.rainbow.domain.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.RoleModifyVO;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.view.util.TiAssert;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 角色信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Getter
@Builder
public class Role implements Entity {

    /** 主键编号 */
    private Long id;
    /** 角色编码 */
    private String code;
    /** 角色名称 */
    private String name;
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
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;

    public void modify(RoleModifyVO modifyVO) {
        this.name = modifyVO.name();
        this.remark = modifyVO.remark();
    }

    public boolean isEnable() {
        return Objects.equals(CommonStatus.ENABLE.code(), status);
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()),
            StrUtil.format("只有[{}]状态才能执行启用操作，角色：{}", disable.message(), name));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()),
            StrUtil.format("只有[{}]状态才能执行禁用操作，角色：{}", enable.message(), name));
        TiAssert.isTrue(!SecurityConst.ADMIN.equals(this.code), StrUtil.format("管理员角色不能被禁用，角色：{}", name));
        this.status = CommonStatus.DISABLE.code();
    }

}