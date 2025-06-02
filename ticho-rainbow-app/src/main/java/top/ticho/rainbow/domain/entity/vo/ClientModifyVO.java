package top.ticho.rainbow.domain.entity.vo;

import java.time.LocalDateTime;

/**
 * @param name     客户端名称
 * @param expireAt 过期时间
 * @param sort     排序
 * @param remark   备注信息
 * @param version  版本号
 * @author zhajianjun
 * @date 2025-03-02 10:50
 */
public record ClientModifyVO(
    String name,
    LocalDateTime expireAt,
    Integer sort,
    String remark,
    Long version
) {
}
