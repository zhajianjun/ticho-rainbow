package top.ticho.rainbow.application.dto;

import lombok.Data;
import top.ticho.rainbow.domain.entity.FileInfo;

import java.util.Objects;

/**
 * 文件缓存
 *
 * @author zhajianjun
 * @date 2024-04-28 09:37
 */
@Data
public class FileCacheDTO {

    /** 签名 */
    private String sign;
    /** 文件信息 */
    private FileInfo fileInfo;
    /* 过期时间戳 */
    private Long expire;
    /* 是否有限制 */
    private Boolean limit;
    /* 是否已限制 */
    private Boolean limited;

    public Long getExpire() {
        // 如果过期时间为空，则返回0
        if (Objects.isNull(expire)) {
            return -1L;
        }
        // 是否有限制，如果不是则返回过期时间
        if (!Boolean.TRUE.equals(limit)) {
            return expire;
        }
        // 如果有限制，并且已限制则返回0，否则返回过期时间
        if (Objects.equals(limited, limit)) {
            return -1L;
        }
        return expire;
    }

}
