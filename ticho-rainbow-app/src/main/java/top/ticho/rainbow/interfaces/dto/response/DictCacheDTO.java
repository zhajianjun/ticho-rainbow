package top.ticho.rainbow.interfaces.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典DTO
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
public class DictCacheDTO {

    /** 主键编号 */
    @NotNull(message = "编号不能为空")
    private Long id;
    /** 字典编码 */
    @NotBlank(message = "编码不能为空")
    private String code;
    /** 字典名称 */
    @NotBlank(message = "名称不能为空")
    private String name;
    /** 是否系统字典;1-是,0-否 */
    private Integer isSys;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 字典标签详情 */
    private List<DictLabelDTO> details;

}
