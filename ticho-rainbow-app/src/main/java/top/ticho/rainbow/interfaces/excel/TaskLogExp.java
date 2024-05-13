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
 * 计划任务日志导出
 *
 * @author zhajianjun
 * @date 2024-05-13 18:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class TaskLogExp implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    @ColumnWidth(20)
    @ExcelProperty(value = "日志编号")
    private String id;

    /** 任务名称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "任务名称")
    private String name;

    /** 任务类型 */
    @ColumnWidth(20)
    @ExcelProperty(value = "任务内容")
    private String content;

    /** 任务参数 */
    @ColumnWidth(20)
    @ExcelProperty(value = "任务参数")
    private String param;

    /** 执行时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "执行时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime executeTime;

    /** 执行开始时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "执行开始时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 执行结束时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "执行结束时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 执行间隔(毫秒) */
    @ColumnWidth(20)
    @ExcelProperty(value = "执行间隔(毫秒)")
    private Integer consume;

    /** mdc信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "mdc信息")
    private String mdc;

    /** 链路id */
    @ColumnWidth(20)
    @ExcelProperty(value = "链路id")
    private String traceId;

    /** 执行状态;1-执行成功,0-执行异常 */
    @ColumnWidth(20)
    @ExcelProperty(value = "执行状态")
    private String statusName;

    /** 是否异常 */
    @ColumnWidth(20)
    @ExcelProperty(value = "是否异常")
    private String isErrName;

    /** 异常信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "异常信息")
    private String errMessage;

    /** 操作人 */
    @ColumnWidth(20)
    @ExcelProperty(value = "操作人")
    private String operateBy;

    /** 创建时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


}
