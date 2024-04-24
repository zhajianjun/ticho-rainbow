package top.ticho.rainbow.interfaces.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import top.ticho.boot.web.util.valid.ValidGroup;

import javax.validation.constraints.NotNull;

/**
 * 文件信息DTO
 *
 * @author zhajianjun
 * @date 2024-04-23 10:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "文件信息DTO")
public class FileInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    @NotNull(message = "编号不能为空", groups = {ValidGroup.Upd.class})
    private Long id;

    /** 存储类型;1-公共,2-私有 */
    @ApiModelProperty(value = "存储类型", position = 20)
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

    /** 文件大小;单位字节 */
    @ApiModelProperty(value = "文件大小;单位字节", position = 60)
    private Long size;

    /** MIME类型 */
    @ApiModelProperty(value = "MIME类型", position = 70)
    private String contentType;

    /** 原始文件名 */
    @ApiModelProperty(value = "原始文件名", position = 80)
    private String originalFilename;

    /** 文件元数据 */
    @ApiModelProperty(value = "文件元数据", position = 90)
    private String metadata;

    /** 状态;1-正常,2-停用,3-分片上传,99-作废 */
    @ApiModelProperty(value = "状态;1-正常,2-停用,3-分片上传,99-作废", position = 100)
    private Integer status;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 110)
    private String remark;

    /** 乐观锁;控制版本更改 */
    @ApiModelProperty(value = "乐观锁;控制版本更改", position = 120)
    private Long version;

    /** 创建人 */
    @ApiModelProperty(value = "创建人", position = 130)
    private String createBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间", position = 140)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /** 更新人 */
    @ApiModelProperty(value = "更新人", position = 150)
    private String updateBy;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间", position = 160)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
