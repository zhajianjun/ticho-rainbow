package top.ticho.intranet.server.interfaces.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户登录账号查询信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@ApiModel(value = "用户登录账号查询信息")
public class UserAccountQuery {

    @ApiModelProperty(value = "登录账户", position = 20)
    private String username;

    @ApiModelProperty(value = "邮箱", position = 30)
    private String email;

    @ApiModelProperty(value = "手机号码", position = 40)
    private String mobile;

    @ApiModelProperty(value = "用户状态", position = 50)
    private List<Integer> status;

}
