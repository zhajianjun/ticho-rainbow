package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 用户角色信息DTO
 *
 * @author zhajianjun
* @date 2023-12-17 08:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户角色信息DTO")
public class UserRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户id */
    @ApiModelProperty(value = "用户id", position = 10)
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /** 角色id列表 */
    @NotEmpty(message = "角色id列表不能为空")
    @ApiModelProperty(value = "角色id列表", position = 20)
    private List<Long> roleIds;

}
