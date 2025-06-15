package top.ticho.rainbow.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 端口信息查询条件
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PortQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 主键编号 */
    private Long id;
    /** 客户端秘钥 */
    private String accessKey;
    /** 主机端口 */
    private Integer port;
    /** 客户端地址 */
    private String endpoint;
    /** 域名 */
    private String domain;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireAt;
    /** 协议类型 */
    private Integer type;
    /** 备注信息 */
    private String remark;

}
