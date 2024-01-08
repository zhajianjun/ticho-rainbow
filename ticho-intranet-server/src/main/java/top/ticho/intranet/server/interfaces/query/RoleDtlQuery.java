package top.ticho.intranet.server.interfaces.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色菜单查询条件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "角色菜单查询条件")
public class RoleDtlQuery {

    /** 角色code */
    @ApiModelProperty(value = "角色id列表", position = 10)
    private List<Long> roleIds;

    /** 角色code */
    @ApiModelProperty(value = "角色code列表", position = 30)
    private List<String> roleCodes;

    /** 显示所有 */
    @ApiModelProperty(value = "是否显示所有", position = 40)
    private Boolean showAll = false;

    /** 是否进行树化 */
    @ApiModelProperty(value = "是否进行树化", position = 40)
    private Boolean treeHandle = false;

}
