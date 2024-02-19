package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 分片文件信息
 *
 * @author zhajianjun
 * @date 2024-02-19 12:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChunkFileDTO extends ChunkDTO {

    @NotNull(message = "文件不能空")
    @ApiModelProperty(value = "文件", required = true, position = 10000)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile file;

    @NotNull(message = "分片索引不能为空")
    @ApiModelProperty(value = "分片索引", required = true, position = 10010)
    private Integer index;

}
