package top.ticho.rainbow.application.dto.command;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    /** 分片id */
    @NotBlank(message = "分片id不能空")
    private String chunkId;
    /** 是否续传 */
    @NotNull(message = "是否续传不能空")
    private Boolean isContinued;
    /** md5 */
    private String md5;
    /** 文件名 */
    @Size(max = 100, message = "文件名过长，1-100字符以内！")
    private String fileName;
    /** 文件大小 */
    @NotNull(message = "文件大小不能空")
    private Long fileSize;
    /** 分片数量 */
    @NotNull(message = "分片数量不能为空")
    @Min(value = 1, message = "分片数量最小为1")
    private Integer chunkCount;
    /** 分片文件 */
    @NotNull(message = "分片文件不能空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile chunkfile;
    /** 存储类型;1-公共,2-私有 */
    private Integer type = 2;
    /** 相对位置，不包含文件名 */
    private String relativePath;
    /** 分片索引 */
    @NotNull(message = "分片索引不能为空")
    private Integer index;

}
