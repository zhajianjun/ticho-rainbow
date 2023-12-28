package top.ticho.intranet.server.interfaces.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ticho.boot.view.core.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息查询条件
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "用户信息查询条件")
public class UserQuery extends BasePageQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户名 */
    @ApiModelProperty(value = "用户名", position = 20)
    private String username;

    /** 备注信息 */
    @ApiModelProperty(value = "备注信息", position = 40)
    private String remark;

}
