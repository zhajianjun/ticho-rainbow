package top.ticho.rainbow.interfaces.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 版本修改
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class VersionModifyCommand {

    /** 编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 版本号 */
    @NotNull(message = "版本号不能为空")
    private Long version;

}
