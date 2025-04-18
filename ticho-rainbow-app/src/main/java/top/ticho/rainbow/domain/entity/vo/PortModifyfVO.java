package top.ticho.rainbow.domain.entity.vo;

import java.time.LocalDateTime;

/**
 * 端口信息DTO
 *
 * @param accessKey 客户端秘钥
 * @param port      主机端口
 * @param endpoint  客户端地址
 * @param domain    域名
 * @param status    状态;1-启用,0-停用
 * @param expireAt  过期时间
 * @param type      协议类型
 * @param sort      排序
 * @param remark    备注信息
 * @param version   版本号
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public record PortModifyfVO(
    String accessKey,
    Integer port,
    String endpoint,
    String domain,
    Integer status,
    LocalDateTime expireAt,
    Integer type,
    Integer sort,
    String remark,
    Long version
) {

}
