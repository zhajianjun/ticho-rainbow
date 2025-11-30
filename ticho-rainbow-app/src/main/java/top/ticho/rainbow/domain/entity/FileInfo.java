package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;
import top.ticho.rainbow.infrastructure.common.enums.FileInfoStatus;
import top.ticho.tool.core.TiAssert;
import top.ticho.tool.core.TiStrUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 文件信息
 *
 * @author zhajianjun
 * @date 2024-04-23 10:41
 */
@Getter
@Builder
public class FileInfo implements Entity {

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
    /** 分片元数据 */
    private String chunkMetadata;
    /** md5 */
    private String md5;
    /** 状态;1-启用,2-停用,3-分片上传,99-作废 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    private LocalDateTime updateTime;

    public void compose(long size) {
        this.size = size;
        this.status = FileInfoStatus.ENABLE.code();
    }

    public boolean isEnable() {
        return Objects.equals(FileInfoStatus.ENABLE.code(), this.status);
    }

    public boolean isCancel() {
        return Objects.equals(this.status, FileInfoStatus.CANCEL.code());
    }

    public boolean isChunk() {
        return Objects.equals(this.status, FileInfoStatus.CHUNK.code());
    }

    public void enable() {
        FileInfoStatus disable = FileInfoStatus.DISABLE;
        TiAssert.isTrue(Objects.equals(this.status, disable.code()), TiStrUtil.format("只有[{}]状态才能执行启用操作，文件：{}", disable.message(), fileName));
        this.status = FileInfoStatus.ENABLE.code();
    }

    public void disable() {
        FileInfoStatus enable = FileInfoStatus.ENABLE;
        TiAssert.isTrue(Objects.equals(this.status, enable.code()), TiStrUtil.format("只有[{}]状态才能执行禁用操作，文件：{}", enable.message(), fileName));
        this.status = FileInfoStatus.DISABLE.code();
    }

    public void cancel() {
        FileInfoStatus cancel = FileInfoStatus.CANCEL;
        TiAssert.isTrue(!Objects.equals(this.status, cancel.code()), TiStrUtil.format("文件：[{}]已作废", cancel.message(), fileName));
        this.status = cancel.code();
    }


}