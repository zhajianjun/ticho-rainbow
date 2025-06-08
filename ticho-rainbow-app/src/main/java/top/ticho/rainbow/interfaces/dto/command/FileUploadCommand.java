package top.ticho.rainbow.interfaces.dto.command;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 文件上传
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Data
public class FileUploadCommand {

    /** 文件 */
    @NotNull(message = "文件不能空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private MultipartFile file;
    /** 1-公共,2-私有 */
    private Integer type = 2;
    /** 相对位置，不包含文件名 */
    private String relativePath;
    /** 备注 */
    @Size(max = 50, min = 1, message = "备注过长，{min}-{max}字符以内！")
    private String remark;

}
