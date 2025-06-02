package top.ticho.rainbow.domain.entity;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.ClientModifyVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.view.util.TiAssert;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 客户端信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Getter
@Builder
public class Client implements Entity {

    /** 主键标识 */
    private Long id;
    /** 客户端秘钥 */
    private String accessKey;
    /** 客户端名称 */
    private String name;
    /** 过期时间 */
    private LocalDateTime expireAt;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
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

    public void modify(ClientModifyVO vo) {
        this.name = vo.name();
        this.expireAt = vo.expireAt();
        this.sort = vo.sort();
        this.remark = vo.remark();
        this.version = vo.version();
    }

    public boolean isEnable() {
        return Objects.equals(CommonStatus.ENABLE.code(), status);
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()),
            StrUtil.format("只有[{}]状态才能执行启用操作，客户端：{}", disable.message(), name));
        boolean isExired = Objects.nonNull(expireAt) && expireAt.isBefore(LocalDateTime.now());
        TiAssert.isTrue(!isExired, StrUtil.format("客户端：{}已过期", name));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()),
            StrUtil.format("只有[{}]状态才能执行禁用操作，客户端：{}", enable.message(), name));
        this.status = CommonStatus.DISABLE.code();
    }

}