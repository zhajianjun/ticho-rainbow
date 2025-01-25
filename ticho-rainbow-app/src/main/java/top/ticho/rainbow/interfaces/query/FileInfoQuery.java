package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息查询条件
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "文件信息查询条件")
public class FileInfoQuery extends TiPageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号列表 */
    @ApiModelProperty(value = "主键编号列表", position = 9)
    private List<Long> ids;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 存储类型;1-公共,2-私有 */
    @ApiModelProperty(value = "存储类型;1-公共,2-私有", position = 20)
    private Integer type;

    /** 文件名 */
    @ApiModelProperty(value = "文件名", position = 30)
    private String fileName;

    /** 文件扩展名 */
    @ApiModelProperty(value = "文件扩展名", position = 40)
    private String ext;

    /** 存储路径 */
    @ApiModelProperty(value = "存储路径", position = 50)
    private String path;

    /** 文件大小开始 */
    @ApiModelProperty(value = "文件大小开始", position = 62)
    private Long sizeStart;

    /** 文件大小结束 */
    @ApiModelProperty(value = "文件大小结束", position = 64)
    private Long sizeEnd;

    /** MIME类型 */
    @ApiModelProperty(value = "MIME类型", position = 70)
    private String contentType;

    /** 原始文件名 */
    @ApiModelProperty(value = "原始文件名", position = 80)
    private String originalFileName;

    /** 状态;1-正常,2-停用,3-分片上传,99-作废 */
    @ApiModelProperty(value = "状态;1-正常,2-停用,3-分片上传,99-作废", position = 100)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 110)
    private String remark;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 130)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 140)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] createTime;

    /** 修改人 */
    @ApiModelProperty(value = "修改人", position = 150)
    private String updateBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 160)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] updateTime;

}
