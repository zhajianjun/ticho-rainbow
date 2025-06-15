package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 配置信息DTO
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Data
public class SettingDTO {

    /** 主键编号 */
    private Long id;
    /** key */
    private String key;
    /** value */
    private String value;
    /** 排序 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 版本号 */
    private Long version;
    /** 创建人 */
    private String createBy;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /** 修改人 */
    private String updateBy;
    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}