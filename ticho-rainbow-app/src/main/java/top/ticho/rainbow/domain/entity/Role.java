package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.RoleModifyVO;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;

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
public class Role {

    /** 主键编号 */
    private Long id;
    /** 角色编码 */
    private String code;
    /** 角色名称 */
    private String name;
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
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;

    public void modify(RoleModifyVO modifyVO) {
        this.name = modifyVO.getName();
        this.status = modifyVO.getStatus();
        this.remark = modifyVO.getRemark();
        this.version = modifyVO.getVersion();
        modifyStatus(modifyVO.getStatus(), modifyVO.getVersion());
    }

    public void modifyStatus(Integer status, Long version) {
        this.status = status;
        this.version = version;
        // 管理员角色一定是正常状态
        if (Objects.equals(SecurityConst.ADMIN, code)) {
            this.status = CommonStatus.ENABLE.code();
        }
    }

}