package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String chunkId;
    private String md5;
    private Long id;
    private String originalFileName;
    private String fileName;
    private Long fileSize;
    private Integer type;
    private Integer chunkCount;
    private AtomicInteger uploadedChunkCount;
    private String extName;
    private String contentType;
    private Boolean complete;
    @JsonIgnore
    private String chunkDirPath;
    @JsonIgnore
    private String relativeFullPath;
    @JsonIgnore
    private ConcurrentSkipListSet<Integer> indexs;

}
