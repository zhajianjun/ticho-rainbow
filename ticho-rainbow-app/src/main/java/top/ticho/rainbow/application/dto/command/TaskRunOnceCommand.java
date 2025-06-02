package top.ticho.rainbow.application.dto.command;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author zhajianjun
 * @date 2025-06-02 18:38
 */
@Data
public class TaskRunOnceCommand {

    /** 编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 参数 */
    private String param;

}
