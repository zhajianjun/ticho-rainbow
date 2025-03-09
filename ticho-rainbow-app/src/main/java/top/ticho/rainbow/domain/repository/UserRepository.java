package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.application.dto.query.UserAccountQuery;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.domain.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息 repository接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
public interface UserRepository {

    boolean save(User user);

    boolean saveBatch(List<User> users);

    boolean modify(User user);

    /**
     * 根据条件查询用户信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link User}>
     */
    List<User> list(UserQuery query);

    /**
     * 根据用户名查询(缓存)
     *
     * @param username 用户名
     * @return {@link User}
     */
    User getCacheByUsername(String username);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return {@link User}
     */
    User getByUsername(String username);

    /**
     * 根据用户名删除
     *
     * @param username 用户名
     * @return {@link User}
     */
    boolean removeByUsername(String username);

    /**
     * 根据用户名列表查询
     *
     * @param usernames  用户名列表
     * @param status     更新状态
     * @param eqDbStatus 必须是某些状态
     * @param neDbStatus 不能更新的状态
     * @return {@link User}
     */
    Integer updateStatus(Collection<String> usernames, Integer status, Collection<Integer> eqDbStatus, Collection<Integer> neDbStatus);

    /**
     * @param email 邮箱
     * @return {@link User}
     */
    User getByEmail(String email);

    /**
     * 根据用户登录账号信息查询
     *
     * @param userAccountQuery 用户登录账号信息
     * @return 用户信息
     */
    List<User> getByAccount(UserAccountQuery userAccountQuery);

}

