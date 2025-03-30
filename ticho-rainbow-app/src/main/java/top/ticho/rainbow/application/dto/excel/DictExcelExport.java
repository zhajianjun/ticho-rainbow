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
 * 字典信息导出
 *
 * @author zhajianjun
 * @date 2024-05-13 18:00
 */
@Data
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class DictExcelExport {

    /** 字典编码 */
    @ColumnWidth(20)
    @ExcelProperty(value = "字典编码")
    private String code;
    /** 字典名称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "字典名称")
    private String name;
    /** 字典标签 */
    @ColumnWidth(20)
    @ExcelProperty(value = "字典标签")
    private String label;
    /** 字典编码 */
    @ColumnWidth(20)
    @ExcelProperty(value = "字典值")
    private String value;
    /** 图标 */
    @ColumnWidth(20)
    @ExcelProperty(value = "图标")
    private String icon;
    /** 颜色 */
    @ColumnWidth(20)
    @ExcelProperty(value = "颜色")
    private String color;
    /** 排序 */
    @ColumnWidth(20)
    @ExcelProperty(value = "排序")
    private Integer sort;
    /** 状态;1-正常,0-禁用 */
    @ColumnWidth(20)
    @ExcelProperty(value = "字典状态")
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
