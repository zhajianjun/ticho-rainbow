package top.ticho.rainbow.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import top.ticho.starter.web.util.valid.TiValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class RoleDTO {

    /** 主键编号 */
    @NotBlank(message = "角色编号不能为空", groups = {TiValidGroup.Upd.class})
    private Long id;
    /** 角色编码 */
    @NotBlank(message = "角色编码不能为空")
    private String code;
    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    private String name;
    /** 状态;1-正常,0-禁用 */
    @NotNull(message = "角色状态不能为空")
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 菜单id列表 */
    private List<Long> menuIds;

}
