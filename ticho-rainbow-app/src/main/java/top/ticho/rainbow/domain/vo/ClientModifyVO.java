package top.ticho.rainbow.domain.vo;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * @author zhajianjun
 * @date 2025-03-02 10:50
 */
@Value
public class ClientModifyVO {
    /** 客户端名称 */
    String name;
    /** 过期时间 */
    LocalDateTime expireAt;
    /** 状态;1-启用,0-停用 */
    Integer status;
    /** 排序 */
    Integer sort;
    /** 备注信息 */
    String remark;
    /** 版本号 */
    Long version;
}
