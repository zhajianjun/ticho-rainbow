package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 角色修改
 *
 * @author zhajianjun
 * @date 2025-03-08 14:44
 */
@Data
public class RoleModifyCommand {

    /** 编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /** 状态;1-正常,0-禁用 */
    @NotNull(message = "角色状态不能为空")
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;
    /** 菜单id列表 */
    private List<Long> menuIds;

}
