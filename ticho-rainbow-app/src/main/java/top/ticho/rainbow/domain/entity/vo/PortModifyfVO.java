package top.ticho.rainbow.domain.entity.vo;

import java.time.LocalDateTime;

/**
 * 端口信息VO
 *
 * @param accessKey 客户端秘钥
 * @param port      主机端口
 * @param endpoint  客户端地址
 * @param domain    域名
 * @param expireAt  过期时间
 * @param type      协议类型
 * @param sort      排序
 * @param remark    备注信息
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public record PortModifyfVO(
    String accessKey,
    Integer port,
    String endpoint,
    String domain,
    LocalDateTime expireAt,
    Integer type,
    Integer sort,
    String remark
) {

}
