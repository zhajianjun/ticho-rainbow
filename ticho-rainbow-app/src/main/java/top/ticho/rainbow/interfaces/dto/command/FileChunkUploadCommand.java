package top.ticho.rainbow.interfaces.dto.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 分片文件上传
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
    @Size(max = 100, min = 1, message = "文件名过长，{min}-{max}字符以内！")
    private String fileName;
    /** 文件大小 */
    @NotNull(message = "文件大小不能空")
    private Long fileSize;
    /** 分片数量 */
    @NotNull(message = "分片数量不能为空")
    @Min(value = 1, message = "分片数量最小为{value}")
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
