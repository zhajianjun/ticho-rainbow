package top.ticho.rainbow.application.dto.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.format.DateTimeFormat;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentFontStyle;
import cn.idev.excel.annotation.write.style.HeadFontStyle;
import cn.idev.excel.annotation.write.style.HeadStyle;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文件信息导出
 *
 * @author zhajianjun
 * @date 2024-05-14 18:00
 */
@Data
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class FileInfoExcelExport {

    /** 存储类型;1-公共,2-私有 */
    @ColumnWidth(20)
    @ExcelProperty(value = "存储类型")
    private String typeName;
    /** 文件名 */
    @ColumnWidth(20)
    @ExcelProperty(value = "文件名")
    private String fileName;
    /** 文件扩展名 */
    @ColumnWidth(20)
    @ExcelProperty(value = "文件扩展名")
    private String ext;
    /** 存储路径 */
    @ColumnWidth(20)
    @ExcelProperty(value = "存储路径")
    private String path;
    /** 文件大小;单位字节 */
    @ColumnWidth(20)
    @ExcelProperty(value = "文件大小")
    private String size;
    /** MIME类型 */
    @ColumnWidth(20)
    @ExcelProperty(value = "MIME类型")
    private String contentType;
    /** 原始文件名 */
    @ColumnWidth(20)
    @ExcelProperty(value = "原始文件名")
    private String originalFileName;
    /** 文件元数据 */
    @ColumnWidth(20)
    @ExcelProperty(value = "文件元数据")
    private String metadata;
    /** 分片id */
    @ColumnWidth(20)
    @ExcelProperty(value = "分片id")
    private String chunkId;
    /** 状态;1-启用,2-停用,3-分片上传,99-作废 */
    @ColumnWidth(20)
    @ExcelProperty(value = "文件状态")
    private String statusName;
    /** 备注信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "备注信息")
    private String remark;
    /** 创建人 */
    @ColumnWidth(20)
    @ExcelProperty(value = "创建人")
    private String createBy;
    /** 创建时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /** 修改人 */
    @ColumnWidth(20)
    @ExcelProperty(value = "修改人")
    private String updateBy;
    /** 修改时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "修改时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
