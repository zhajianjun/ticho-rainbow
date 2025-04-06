package top.ticho.rainbow.application.dto;

import lombok.Data;

/**
 * 分片元数据DTO
 *
 * @author zhajianjun
 * @date 2024-04-25 18:00
 */
@Data
public class ChunkMetadataDTO {

    private String chunkId;
    private Integer chunkCount;
    private Long chunkSize;
    private String chunkDirPath;

}
