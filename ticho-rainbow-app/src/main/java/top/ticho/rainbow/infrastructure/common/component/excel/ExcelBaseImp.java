package top.ticho.rainbow.infrastructure.common.component.excel;

import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;

/**
 * 导入返回信息
 *
 * @author zhajianjun
 * @date 2024-05-09 18:41
 */
@Data
public class ExcelBaseImp implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 错误信息 */
    @ExcelProperty(value = "错误信息")
    @ColumnWidth(25)
    private String message;
    /** 数据是否异常 */
    @ExcelIgnore
    private Boolean isError = false;
}
