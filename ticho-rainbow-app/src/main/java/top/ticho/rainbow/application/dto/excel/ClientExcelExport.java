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
 * 客户端信息导出
 *
 * @author zhajianjun
 * @date 2024-05-14 18:00
 */
@Data
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class ClientExcelExport {

    /** 客户端秘钥 */
    @ColumnWidth(20)
    @ExcelProperty(value = "客户端秘钥")
    private String accessKey;
    /** 客户端名称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "客户端名称")
    private String name;
    /** 过期时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "过期时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireAt;
    /** 状态;1-启用,0-禁用 */
    @ColumnWidth(20)
    @ExcelProperty(value = "客户端状态")
    private String statusName;
    /** 排序 */
    @ColumnWidth(20)
    @ExcelProperty(value = "排序")
    private Integer sort;
    /** 备注信息 */
    @ColumnWidth(20)
    @ExcelProperty(value = "备注信息")
    private String remark;
    /** 连接时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "连接时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime connectTime;
    /** 通道状态;1-激活,0-未激活 */
    @ColumnWidth(20)
    @ExcelProperty(value = "通道状态")
    private String channelStatusName;
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
