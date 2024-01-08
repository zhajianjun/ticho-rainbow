package top.ticho.intranet.server.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据字典查询条件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "数据字典查询条件")
public class DictQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 父id */
    @ApiModelProperty(value = "父id", position = 20)
    private Long parentId;

    /** 字典类型id */
    @ApiModelProperty(value = "字典类型id", position = 30)
    private Long typeId;

    /** 字典编码 */
    @ApiModelProperty(value = "字典编码", position = 40)
    private String code;

    /** 字典名称 */
    @ApiModelProperty(value = "字典名称", position = 50)
    private String name;

    /** 排序 */
    @ApiModelProperty(value = "排序", position = 60)
    private Integer sort;

    /** 层级 */
    @ApiModelProperty(value = "层级", position = 70)
    private Integer level;

    /** 结构 */
    @ApiModelProperty(value = "结构", position = 80)
    private String structure;

    /** 状态;0-禁用,1-正常 */
    @ApiModelProperty(value = "状态;0-禁用,1-正常", position = 90)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 100)
    private String remark;

    /** 乐观锁;控制版本更改 */
    @ApiModelProperty(value = "乐观锁;控制版本更改", position = 110)
    private Long version;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 120)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 130)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 140)
    private String updateBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 150)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /** 删除标识;0-未删除,1-已删除 */
    @ApiModelProperty(value = "删除标识;0-未删除,1-已删除", position = 160)
    private Integer isDelete;

}
