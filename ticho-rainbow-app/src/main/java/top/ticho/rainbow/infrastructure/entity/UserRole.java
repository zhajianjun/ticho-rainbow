package top.ticho.rainbow.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户角色关联关系
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user_role")
public class UserRole extends Model<UserRole> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户id */
    private Long userId;

    /** 角色id */
    private Long roleId;

}