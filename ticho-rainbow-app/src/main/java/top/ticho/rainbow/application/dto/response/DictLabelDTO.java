package top.ticho.rainbow.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典标签DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class DictLabelDTO {

    /** 主键编号 */
    private Long id;
    /** 字典编码 */
    private String code;
    /** 字典标签 */
    private String label;
    /** 字典值 */
    private String value;
    /** 图标 */
    private String icon;
    /** 颜色 */
    private String color;
    /** 排序 */
    private Integer sort;
    /** 状态;1-启用,0-停用 */
    private Integer status;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}
