package top.ticho.intranet.server.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.web.util.valid.ValidGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典类型DTO
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "字典类型DTO")
public class DictTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    @NotNull(message = "编号不能为空", groups = {ValidGroup.Upd.class})
    private Long id;

    /** 字典编码 */
    @ApiModelProperty(value = "字典编码", position = 20)
    @NotBlank(message = "编码不能为空", groups = {ValidGroup.Add.class})
    private String code;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称", position = 30)
    @NotBlank(message = "名称不能为空", groups = {ValidGroup.Add.class})
    private String name;

    /** 是否系统字典;1-是,0-否 */
    @ApiModelProperty(value = "是否系统字典;1-是,0-否", position = 40)
    private Integer isSys;

    /** 状态;1-正常,0-停用 */
    @ApiModelProperty(value = "状态;1-正常,0-停用", position = 50)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 60)
    private String remark;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 70)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "字典详情", position = 70)
    private List<DictDTO> details;

}
