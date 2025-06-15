package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 日志信息查询条件
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpLogQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 主键编号 */
    private Long id;
    /** 名称 */
    private String name;
    /** 请求地址 */
    private String url;
    /** 请求类型 */
    private String type;
    /** 请求体 */
    private String reqBody;
    /** 请求参数 */
    private String reqParams;
    /** 响应体 */
    private String resBody;
    /** 请求开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] startTime;
    /** 请求结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] endTime;
    /** 请求间隔开始 */
    private Integer consumeStart;
    /** 请求间隔结束 */
    private Integer consumeEnd;
    private String traceId;
    /** 请求IP */
    private String ip;
    /** 响应状态 */
    private Integer resStatus;
    /** 操作人 */
    private String operateBy;
    /** 是否异常 */
    private Integer isErr;
    /** 异常信息 */
    private String errMessage;

}
