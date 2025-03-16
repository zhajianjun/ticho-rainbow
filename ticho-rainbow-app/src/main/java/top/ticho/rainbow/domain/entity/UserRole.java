package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;

/**
 * 用户角色关联关系
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Getter
@Builder
public class UserRole {

    /** 用户id */
    private Long userId;
    /** 角色id */
    private Long roleId;
}