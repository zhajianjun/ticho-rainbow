package top.ticho.rainbow.interfaces.query;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import top.ticho.boot.view.core.BasePageQuery;

/**
 * 文件信息查询条件
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "文件信息查询条件")
public class FileInfoQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 存储类型 */
    @ApiModelProperty(value = "存储类型", position = 20)
    private Integer type;

    /** 文件名称 */
    @ApiModelProperty(value = "文件名称", position = 30)
    private String fileName;

    /** 文件扩展名 */
    @ApiModelProperty(value = "文件扩展名", position = 40)
    private String ext;

    /** 存储路径 */
    @ApiModelProperty(value = "存储路径", position = 50)
    private String path;

    /** 文件大小;单位字节 */
    @ApiModelProperty(value = "文件大小;单位字节", position = 60)
    private Long size;

    /** MIME类型 */
    @ApiModelProperty(value = "MIME类型", position = 70)
    private String contentType;

    /** 原始文件名 */
    @ApiModelProperty(value = "原始文件名", position = 80)
    private String originalFilename;

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

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 150)
    private String updateBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 160)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] updateTime;

}
