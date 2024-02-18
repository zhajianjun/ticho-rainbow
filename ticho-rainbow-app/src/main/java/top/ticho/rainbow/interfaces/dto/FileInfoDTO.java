package top.ticho.rainbow.interfaces.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件表
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Data
@ApiModel(value = "文件信息")
public class FileInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源id", required = true, position = 10)
    private String storageId;

    @ApiModelProperty(value = "文件名", position = 20)
    private String fileName;

    @ApiModelProperty(value = "文件类型", position = 30)
    private String contentType;

    @ApiModelProperty(value = "文件大小。", position = 30)
    private String size;

    @ApiModelProperty(value = "备注", position = 40)
    private String remark;

    @ApiModelProperty(value = "存储桶名称", example = "default", position = 50)
    private String bucket;
}
