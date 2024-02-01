package top.ticho.rainbow.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 日志信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "日志信息DTO")
public class OpLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号; */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 请求地址 */
    @ApiModelProperty(value = "请求地址", position = 20)
    private String url;

    /** 日志类型 */
    @ApiModelProperty(value = "日志类型", position = 30)
    private String type;

    /** 请求方法 */
    @ApiModelProperty(value = "请求方法", position = 40)
    private String method;

    /** 请求参数 */
    @ApiModelProperty(value = "请求参数", position = 50)
    private String params;

    /** 内容 */
    @ApiModelProperty(value = "内容", position = 60)
    private String message;

    /** 总耗时长（毫秒） */
    @ApiModelProperty(value = "总耗时长（毫秒）", position = 70)
    private Integer totalTime;

    /** 请求IP */
    @ApiModelProperty(value = "请求IP", position = 80)
    private String ip;

    /** 操作人 */
    @ApiModelProperty(value = "操作人", position = 90)
    private String operateBy;

    /** 操作时间 */
    @ApiModelProperty(value = "操作时间", position = 100)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime operateTime;

    /** 是否异常 */
    @ApiModelProperty(value = "是否异常", position = 110)
    private Integer isErr;

    /** 异常信息 */
    @ApiModelProperty(value = "异常信息", position = 120)
    private String errMessage;

}
