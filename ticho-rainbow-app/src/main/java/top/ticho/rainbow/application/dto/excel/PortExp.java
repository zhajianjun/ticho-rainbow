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
 * 端口信息导出
 *
 * @author zhajianjun
 * @date 2024-05-14 18:00
 */
@Data
@HeadFontStyle(fontHeightInPoints = 12)
@HeadStyle(fillForegroundColor = 1, leftBorderColor = 22, rightBorderColor = 22, bottomBorderColor = 57)
@ContentFontStyle(fontHeightInPoints = 10)
public class PortExp {

    /** 客户端名称 */
    @ColumnWidth(20)
    @ExcelProperty(value = "客户端名称")
    private String clientName;
    /** 主机端口 */
    @ColumnWidth(20)
    @ExcelProperty(value = "主机端口")
    private Integer port;
    /** 客户端地址 */
    @ColumnWidth(20)
    @ExcelProperty(value = "客户端地址")
    private String endpoint;
    /** 域名 */
    @ColumnWidth(20)
    @ExcelProperty(value = "域名")
    private String domain;
    /** 状态;1-启用,0-停用 */
    @ColumnWidth(20)
    @ExcelProperty(value = "端口状态")
    private String statusName;
    /** 过期时间 */
    @ColumnWidth(20)
    @ExcelProperty(value = "过期时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireAt;
    /** 协议类型 */
    @ColumnWidth(20)
    @ExcelProperty(value = "协议类型")
    private String typeName;
    /** 客户端状态;1-激活,0-未激活 */
    @ColumnWidth(20)
    @ExcelProperty(value = "客户端通道状态")
    private String clientChannelStatusName;
    /** 通道状态;1-激活,0-未激活 */
    @ColumnWidth(20)
    @ExcelProperty(value = "应用通道状态")
    private String appChannelStatusName;
    /** 排序 */
    @ColumnWidth(20)
    @ExcelProperty(value = "排序")
    private Integer sort;
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
