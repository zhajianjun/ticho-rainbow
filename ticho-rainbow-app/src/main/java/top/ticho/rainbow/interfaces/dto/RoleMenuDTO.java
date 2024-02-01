package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单关联关系
 *
 * @author zhajianjun
* @date 2023-12-17 08:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "角色菜单信息DTO")
public class RoleMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 角色id */
    @ApiModelProperty(value = "角色id", position = 10)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /** 菜单id列表 */
    @ApiModelProperty(value = "菜单id列表", position = 20)
    private List<Long> menuIds;

}