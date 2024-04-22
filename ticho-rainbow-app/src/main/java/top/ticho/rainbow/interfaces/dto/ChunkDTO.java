package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 分片信息
 *
 * @author zhajianjun
 * @date 2024-02-19 12:08
 */
@Data
public class ChunkDTO {

    @NotNull(message = "md5不能空")
    @ApiModelProperty(value = "md5", required = true, position = 10)
    private String md5;

    @Size(max = 20, message = "文件名过长，1-20字符以内！")
    @ApiModelProperty(value = "文件名，如果没有则默认使用MultipartFile中的文件名", required = true, position = 20)
    private String fileName;

    @NotNull(message = "分片数量不能为空")
    @Min(value = 1, message = "分片数量最小为1")
    @ApiModelProperty(value = "分片数量", required = true, position = 30)
    private Integer chunkCount;

    @ApiModelProperty(value = "minio文件名", position = 40)
    private String objectName;

    @ApiModelProperty(value = "文件后缀名，如：png", position = 50)
    private String extName;

    @ApiModelProperty(value = "路径", position = 50)
    private String path;

    @ApiModelProperty(value = "已经上传的分片索引", position = 60, hidden = true)
    @JsonIgnore
    private ConcurrentSkipListSet<Integer> indexs;

    @ApiModelProperty(value = "分片上传是否完成", position = 70)
    private Boolean complete;

}
