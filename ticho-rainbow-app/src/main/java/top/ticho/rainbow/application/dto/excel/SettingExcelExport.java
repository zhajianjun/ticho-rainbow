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
 * 配置信息Excel导出
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Data
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class SettingExcelExport {

    /** key */
    @ColumnWidth(20)
    @ExcelProperty(value = "key")
    private String key;
    /** value */
    @ColumnWidth(20)
    @ExcelProperty(value = "value")
    private String value;
    /** 排序 */
    @ColumnWidth(20)
    @ExcelProperty(value = "排序")
    private Integer sort;
    /** 备注信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "备注信息")
    private String remark;
    /** 版本号 */
    @ColumnWidth(20)
    @ExcelProperty(value = "版本号")
    private Long version;
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