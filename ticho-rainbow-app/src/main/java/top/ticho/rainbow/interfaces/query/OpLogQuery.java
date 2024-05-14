package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.boot.view.core.BasePageQuery;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志信息查询条件
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "日志信息查询条件")
public class OpLogQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键编号列表 */
    @ApiModelProperty(value = "主键编号列表", position = 9)
    private List<Long> ids;

    /** 主键编号 */
    @ApiModelProperty(value = "主键编号", position = 10)
    private Long id;

    /** 名称 */
    @ApiModelProperty(value = "名称", position = 20)
    private String name;

    /** 请求地址 */
    @ApiModelProperty(value = "请求地址", position = 30)
    private String url;

    /** 请求类型 */
    @ApiModelProperty(value = "请求类型", position = 40)
    private String type;

    /** 请求体 */
    @ApiModelProperty(value = "请求体", position = 60)
    private String reqBody;

    /** 请求参数 */
    @ApiModelProperty(value = "请求参数", position = 70)
    private String reqParams;

    /** 响应体 */
    @ApiModelProperty(value = "响应体", position = 90)
    private String resBody;

    /** 请求开始时间 */
    @ApiModelProperty(value = "请求开始时间", position = 110)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] startTime;

    /** 请求结束时间 */
    @ApiModelProperty(value = "请求结束时间", position = 120)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime[] endTime;

    /** 请求间隔开始 */
    @ApiModelProperty(value = "请求间隔开始", position = 130)
    private Integer consumeStart;

    /** 请求间隔结束 */
    @ApiModelProperty(value = "请求间隔结束", position = 135)
    private Integer consumeEnd;

    @ApiModelProperty(value = "链路id", position = 136)
    private String traceId;

    /** 请求IP */
    @ApiModelProperty(value = "请求IP", position = 140)
    private String ip;

    /** 响应状态 */
    @ApiModelProperty(value = "响应状态", position = 150)
    private Integer resStatus;

    /** 操作人 */
    @ApiModelProperty(value = "操作人", position = 160)
    private String operateBy;

    /** 是否异常 */
    @ApiModelProperty(value = "是否异常", position = 180)
    private Integer isErr;

    /** 异常信息 */
    @ApiModelProperty(value = "异常信息", position = 190)
    private String errMessage;

}
