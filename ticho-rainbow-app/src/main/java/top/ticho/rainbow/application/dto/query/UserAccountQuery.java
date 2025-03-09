package top.ticho.rainbow.application.dto.query;

import lombok.Data;

import java.util.List;

/**
 * 用户登录账号查询信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class UserAccountQuery {

    private String username;
    private String email;
    private String mobile;
    private List<Integer> status;

}
