package top.ticho.rainbow.application.dto.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 分片文件信息
 *
 * @author zhajianjun
 * @date 2024-02-19 12:08
 */
@Data
public class FileChunkUploadCommand {

    @NotBlank(message = "分片id不能空")
    @ApiModelProperty(value = "分片id", required = true, position = 10)
    private String chunkId;

    @NotNull(message = "是否续传不能空")
    @ApiModelProperty(value = "是否续传", required = true, position = 20)
    private Boolean isContinued;

    @NotNull(message = "md5不能空")
    @ApiModelProperty(value = "md5", required = true, position = 30)
    private String md5;

    @Size(max = 100, message = "文件名过长，1-100字符以内！")
    @ApiModelProperty(value = "文件名", required = true, position = 40)
    private String fileName;

    @NotNull(message = "文件大小不能空")
    @ApiModelProperty(value = "文件大小", required = true, position = 50)
    private Long fileSize;

    @NotNull(message = "分片数量不能为空")
    @Min(value = 1, message = "分片数量最小为1")
    @ApiModelProperty(value = "分片数量", required = true, position = 60)
    private Integer chunkCount;

    @NotNull(message = "分片文件不能空")
    @ApiModelProperty(value = "分片文件", required = true, position = 70)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile chunkfile;

    @ApiModelProperty(value = "存储类型;1-公共,2-私有", required = true, position = 80)
    private Integer type = 2;

    @ApiModelProperty(value = "相对位置，不包含文件名", required = true, position = 90)
    private String relativePath;

    @NotNull(message = "分片索引不能为空")
    @ApiModelProperty(value = "分片索引", required = true, position = 100)
    private Integer index;

}
