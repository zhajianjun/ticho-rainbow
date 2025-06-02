package top.ticho.rainbow.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件信息DTO
 *
 * @author zhajianjun
 * @date 2024-04-23 10:41
 */
@Data
public class FileInfoDTO {

    /** 主键编号 */
    private Long id;
    /** 存储类型;1-公共,2-私有 */
    private Integer type;
    /** 文件名 */
    private String fileName;
    /** 文件扩展名 */
    private String ext;
    /** 存储路径 */
    private String path;
    /** 文件大小;单位字节 */
    private Long size;
    /** MIME类型 */
    private String contentType;
    /** 原始文件名 */
    private String originalFileName;
    /** 文件元数据 */
    private String metadata;
    /** 分片id */
    private String chunkId;
    /** 状态;1-启用,2-停用,3-分片上传,99-作废 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
