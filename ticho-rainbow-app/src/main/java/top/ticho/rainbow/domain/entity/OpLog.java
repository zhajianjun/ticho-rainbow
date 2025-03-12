package top.ticho.rainbow.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 日志信息
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Getter
@Builder
public class OpLog {

    /** 主键编号 */
    private Long id;    /** 名称 */
    private String name;    /** 请求地址 */
    private String url;    /** 请求类型 */
    private String type;    /** 请求方法 */
    private String position;    /** 请求体 */
    private String reqBody;    /** 请求参数 */
    private String reqParams;    /** 响应体 */
    private String resBody;    /** 请求开始时间 */
    private LocalDateTime startTime;    /** 请求结束时间 */
    private LocalDateTime endTime;    /** 请求间隔(毫秒) */
    private Integer consume;    /** mdc信息 */
    private String mdc;    /** 链路id */
    private String traceId;    /** 请求IP */
    private String ip;    /** 响应状态 */
    private Integer resStatus;    /** 操作人 */
    private String operateBy;    /** 创建时间 */
    private LocalDateTime createTime;    /** 是否异常 */
    private Integer isErr;    /** 异常信息 */
    private String errMessage;
}