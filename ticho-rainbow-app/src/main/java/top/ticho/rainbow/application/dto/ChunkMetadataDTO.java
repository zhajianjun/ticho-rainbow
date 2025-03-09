package top.ticho.rainbow.application.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 分片元数据DTO
 *
 * @author zhajianjun
 * @date 2024-04-25 18:00
 */
@Data
@ApiModel(value = "分片元数据DTO")
public class ChunkMetadataDTO {

    private String chunkId;

    private Integer chunkCount;

    private Long chunkSize;

    private String chunkDirPath;

}
