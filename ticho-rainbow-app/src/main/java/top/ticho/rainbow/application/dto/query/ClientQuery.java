package top.ticho.rainbow.application.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.ticho.starter.view.core.TiPageQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户端信息查询条件
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientQuery extends TiPageQuery {

    /** 主键编号列表 */
    private List<Long> ids;
    /** 主键编号 */
    private Long id;
    /** 客户端秘钥 */
    private String accessKey;
    /** 客户端名称 */
    private String name;
    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime expireAt;
    /** 状态;1-启用,0-禁用 */
    private Integer status;
    /** 备注信息 */
    private String remark;

}
