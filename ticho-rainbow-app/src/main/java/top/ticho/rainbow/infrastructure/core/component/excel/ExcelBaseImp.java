package top.ticho.rainbow.infrastructure.core.component.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "错误信息")
    private String message;

    /** 数据是否异常 */
    @ExcelIgnore
    @ApiModelProperty(hidden = true)
    private Boolean isError = false;

}
