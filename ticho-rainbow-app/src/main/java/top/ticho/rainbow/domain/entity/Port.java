package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.domain.entity.vo.PortModifyfVO;
import top.ticho.rainbow.infrastructure.common.enums.CommonStatus;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.tool.core.TiStrUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 端口信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Getter
@Builder
public class Port implements Entity {

    /** 主键标识 */
    private Long id;
    /** 客户端秘钥 */
    private String accessKey;
    /** 主机端口 */
    private Integer port;
    /** 客户端地址 */
    private String endpoint;
    /** 域名 */
    private String domain;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 过期时间 */
    private LocalDateTime expireAt;
    /** 协议类型 */
    private Integer type;
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

    public void modify(PortModifyfVO portModifyfVO) {
        if (Objects.equals(CommonStatus.ENABLE.code(), status)) {
            TiAssert.isTrue(Objects.equals(this.accessKey, portModifyfVO.accessKey()), TiStrUtil.format("端口已启用，无法修改秘钥，端口：[{}]"));
            TiAssert.isTrue(Objects.equals(this.port, portModifyfVO.port()), TiStrUtil.format("端口已启用，无法修改端口，端口：[{}]"));
            TiAssert.isTrue(Objects.equals(this.endpoint, portModifyfVO.endpoint()), TiStrUtil.format("端口已启用，无法修改客户端地址，端口：[{}]"));
        }
        this.accessKey = portModifyfVO.accessKey();
        this.port = portModifyfVO.port();
        this.endpoint = portModifyfVO.endpoint();
        this.domain = portModifyfVO.domain();
        this.expireAt = portModifyfVO.expireAt();
        this.type = portModifyfVO.type();
        this.sort = portModifyfVO.sort();
        this.remark = portModifyfVO.remark();
    }

    public boolean isEnable() {
        return Objects.equals(CommonStatus.ENABLE.code(), status);
    }

    public void enable() {
        CommonStatus disable = CommonStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()), TiStrUtil.format("只有[{}]状态才能执行启用操作，端口：[{}]", disable.message(), port));
        boolean isExired = Objects.nonNull(expireAt) && expireAt.isBefore(LocalDateTime.now());
        TiAssert.isTrue(!isExired, TiStrUtil.format("端口[{}]已过期", port));
        this.status = CommonStatus.ENABLE.code();
    }

    public void disable() {
        CommonStatus enable = CommonStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()), TiStrUtil.format("只有[{}]状态才能执行禁用操作，端口：[{}]", enable.message(), port));
        this.status = CommonStatus.DISABLE.code();
    }

}