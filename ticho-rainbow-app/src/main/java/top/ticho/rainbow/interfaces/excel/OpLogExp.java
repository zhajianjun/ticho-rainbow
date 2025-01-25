package top.ticho.rainbow.interfaces.excel;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.format.DateTimeFormat;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import cn.idev.excel.annotation.write.style.ContentFontStyle;
import cn.idev.excel.annotation.write.style.HeadFontStyle;
import cn.idev.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志信息导出
 *
 * @author zhajianjun
 * @date 2024-05-14 18:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class OpLogExp implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 日志编号 */
    @ColumnWidth(20)
    @ExcelProperty(value = "日志编号")
    private String id;

    /** 名称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "名称")
    private String name;

    /** 请求地址 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求地址")
    private String url;

    /** 请求类型 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求类型")
    private String type;

    /** 请求方法 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求方法")
    private String position;

    /** 请求体 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求体")
    private String reqBody;

    /** 请求参数 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求参数")
    private String reqParams;

    /** 响应体 */
    @ColumnWidth(20)
    @ExcelProperty(value = "响应体")
    private String resBody;

    /** 请求开始时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求开始时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 请求结束时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求结束时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 请求间隔(毫秒) */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求间隔(毫秒)")
    private Integer consume;

    /** mdc信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "mdc信息")
    private String mdc;

    /** 链路id */
    @ColumnWidth(20)
    @ExcelProperty(value = "链路id")
    private String traceId;

    /** 请求IP */
    @ColumnWidth(20)
    @ExcelProperty(value = "请求IP")
    private String ip;

    /** 响应状态 */
    @ColumnWidth(20)
    @ExcelProperty(value = "响应状态")
    private String resStatus;

    /** 操作人 */
    @ColumnWidth(20)
    @ExcelProperty(value = "操作人")
    private String operateBy;

    /** 创建时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 是否异常 */
    @ColumnWidth(20)
    @ExcelProperty(value = "是否异常")
    private String isErrName;

    /** 异常信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "异常信息")
    private String errMessage;

}
