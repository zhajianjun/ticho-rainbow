package top.ticho.rainbow.interfaces.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 文件表
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Data
@ApiModel(value = "文件信息")
public class FileInfoReqDTO {

    @ApiModelProperty(value = "文件", required = true, position = 10)
    @NotNull(message = "文件不能空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile file;

    @ApiModelProperty(value = "文件名，如果没有则默认使用MultipartFile中的文件名", required = true, position = 30)
    @Size(max = 20, message = "文件名过长，1-20字符以内！")
    private String fileName;

    @ApiModelProperty(value = "备注", required = true, position = 40)
    @Size(max = 50, message = "备注过长，1-50字符以内！")
    private String remark;

}
