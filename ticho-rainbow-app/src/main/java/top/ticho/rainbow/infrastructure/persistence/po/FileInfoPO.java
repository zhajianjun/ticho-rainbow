package top.ticho.rainbow.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件信息
 *
 * @author zhajianjun
 * @date 2024-04-23 10:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_file_info")
public class FileInfoPO extends Model<FileInfoPO> {

    /** 主键编号 */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
    /** 分片元数据 */
    private String chunkMetadata;
    /** md5 */
    private String md5;
    /** 状态;1-正常,2-停用,3-分片上传,99-作废 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 修改人 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /** 删除标识;0-未删除,1-已删除 */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;
}