package top.ticho.rainbow.interfaces.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色创建
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class RoleSaveCommand {

    /** 角色编码 */
    @NotBlank(message = "角色编码不能为空")
    private String code;
    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /** 备注信息 */
    private String remark;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /** 菜单id列表 */
    private List<Long> menuIds;

}
