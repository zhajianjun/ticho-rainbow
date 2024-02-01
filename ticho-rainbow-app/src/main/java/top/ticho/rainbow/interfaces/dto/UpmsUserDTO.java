package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 权限用户信息DTO
 *
 * @author zhajianjun
 * @date 2020-07-02 20:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "权限用户信息DTO")
public class UpmsUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    @ApiModelProperty(value = "账户", position = 30)
    private String username;

    @ApiModelProperty(value = "密码", position = 40)
    private String password;

    @ApiModelProperty(value = "用户状态;1-正常,2-未激活,3-已锁定,4-已注销", position = 50)
    private Integer status;

    @ApiModelProperty(value = "角色", position = 60)
    private List<String> roleCodes;

    @ApiModelProperty(value = "部门", position = 70)
    private List<String> deptCodes;

}
