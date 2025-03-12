package top.ticho.rainbow.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiSecurityUser;

/**
 * 用户信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SecurityUser extends TiSecurityUser implements UserHelper {

    private String username;
    private Integer status = 2;

    @Override
    public String toString() {
        return super.toString();
    }

}
