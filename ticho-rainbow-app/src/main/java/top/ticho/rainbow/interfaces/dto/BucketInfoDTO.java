package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 文件桶
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Data
@ApiModel(value = "文件桶")
public class BucketInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件存储桶", position = 1)
    private String bucket;

    @ApiModelProperty(value = "创建日期", position = 2)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private ZonedDateTime creationDate;

}
