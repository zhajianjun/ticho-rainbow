package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分片缓存信息
 *
 * @author zhajianjun
 * @date 2024-02-19 12:08
 */
@Data
public class ChunkCacheDTO {

    @ApiModelProperty(value = "分片id", position = 10)
    private String chunkId;

    @ApiModelProperty(value = "md5", position = 20)
    private String md5;

    @ApiModelProperty(value = "id", position = 30)
    private Long id;

    @ApiModelProperty(value = "原始文件名", position = 40)
    private String originalFileName;

    @ApiModelProperty(value = "文件名", position = 50)
    private String fileName;

    @ApiModelProperty(value = "文件大小", position = 55)
    private Long fileSize;

    @ApiModelProperty(value = "存储类型;1-公共,2-私有", position = 60)
    private Integer type;

    @ApiModelProperty(value = "分片数量", position = 70)
    private Integer chunkCount;

    @ApiModelProperty(value = "已经上传的分片数量", position = 80)
    private AtomicInteger uploadedChunkCount;

    @ApiModelProperty(value = "文件后缀名，如：png", position = 90)
    private String extName;

    @ApiModelProperty(value = "MIME类型", position = 100)
    private String contentType;

    @ApiModelProperty(value = "分片上传是否完成", position = 110)
    private Boolean complete;

    @ApiModelProperty(value = "分片文件夹路径", position = 120, hidden = true)
    @JsonIgnore
    private String chunkDirPath;

    @ApiModelProperty(value = "文件相对全路径", position = 130, hidden = true)
    @JsonIgnore
    private String relativeFullPath;

    @ApiModelProperty(value = "已经上传的分片索引", position = 120, hidden = true)
    @JsonIgnore
    private ConcurrentSkipListSet<Integer> indexs;

}
