package top.ticho.rainbow.application.dto.command;


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
public class FileUploadCommand {

    @ApiModelProperty(value = "文件", required = true, position = 10)
    @NotNull(message = "文件不能空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile file;

    @ApiModelProperty(value = "1-公共,2-私有", required = true, position = 20)
    private Integer type = 2;

    @ApiModelProperty(value = "相对位置，不包含文件名", required = true, position = 30)
    private String relativePath;

    @ApiModelProperty(value = "备注", required = true, position = 40)
    @Size(max = 50, message = "备注过长，1-50字符以内！")
    private String remark;

}
