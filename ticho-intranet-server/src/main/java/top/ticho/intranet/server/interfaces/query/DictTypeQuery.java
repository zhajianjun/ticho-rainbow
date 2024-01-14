package top.ticho.intranet.server.interfaces.query;

import java.io.Serializable;
import top.ticho.boot.view.core.BasePageQuery;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 数据字典类型查询条件
 *
 * @author zhajianjun
 * @date 2024-01-14 13:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "数据字典类型查询条件")
public class DictTypeQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 字典编码 */
    @ApiModelProperty(value = "字典编码", position = 20)
    private String code;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称", position = 30)
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

}
