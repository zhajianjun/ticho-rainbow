package top.ticho.rainbow.interfaces.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "分片id", position = 10)
    private String chunkId;

    @ApiModelProperty(value = "分片数量", position = 20)
    private Integer chunkCount;

    @ApiModelProperty(value = "分片大小", position = 30)
    private Long chunkSize;

    @ApiModelProperty(value = "分片文件夹路径", position = 40)
    private String chunkDirPath;

}
