package top.ticho.rainbow.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.rainbow.infrastructure.entity.User;
import top.ticho.rainbow.interfaces.query.UserAccountQuery;
import top.ticho.rainbow.interfaces.query.UserQuery;

import java.util.List;

/**
 * 用户信息 repository接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface UserRepository extends RootService<User> {

    /**
     * 根据条件查询用户信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link User}>
     */
    List<User> list(UserQuery query);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return {@link User}
     */
    User getByUsername(String username);

    /**
     * 根据用户登录账号信息查询
     *
     * @param userAccountQuery 用户登录账号信息
     * @return 用户信息
     */
    List<User> getByAccount(UserAccountQuery userAccountQuery);

}

