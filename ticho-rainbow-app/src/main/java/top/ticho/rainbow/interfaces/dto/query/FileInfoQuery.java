package top.ticho.rainbow.interfaces.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息查询条件
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileInfoQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
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
    /** 文件大小开始 */
    private Long sizeStart;
    /** 文件大小结束 */
    private Long sizeEnd;
    /** MIME类型 */
    private String contentType;
    /** 原始文件名 */
    private String originalFileName;
    /** 状态;1-启用,2-停用,3-分片上传,99-作废 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] updateTime;

}
