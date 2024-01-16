package top.ticho.intranet.server.interfaces.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BaseSecurityUser;

/**
 * 用户信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SecurityUser extends BaseSecurityUser implements UserHelper {

    @ApiModelProperty(value = "账户", position = 20)
    private String username;

    @ApiModelProperty(value = "用户状态;1-正常,2-未激活,3-已锁定,4-已注销", position = 30)
    private Integer status = 2;

    @Override
    public String toString() {
        return super.toString();
    }

}
