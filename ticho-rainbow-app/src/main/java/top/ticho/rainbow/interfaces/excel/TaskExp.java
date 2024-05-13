package top.ticho.rainbow.interfaces.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 计划任务导出
 *
 * @author zhajianjun
 * @date 2024-05-13 18:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class TaskExp implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 任务名称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "任务名称")
    private String name;

    /** 任务类型 */
    @ColumnWidth(20)
    @ExcelProperty(value = "任务类型")
    private String content;

    /** 执行参数 */
    @ColumnWidth(20)
    @ExcelProperty(value = "执行参数")
    private String param;

    /** cron执行表达式 */
    @ColumnWidth(20)
    @ExcelProperty(value = "cron执行表达式")
    private String cronExpression;

    /** 任务状态;1-正常,0-停用 */
    @ColumnWidth(20)
    @ExcelProperty(value = "任务状态")
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
