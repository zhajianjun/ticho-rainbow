package top.ticho.rainbow.domain.repository;

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

    User find(Long id);

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
    Integer modifyStatus(Collection<String> usernames, Integer status, Collection<Integer> eqDbStatus, Collection<Integer> neDbStatus);

    /**
     * @param email 邮箱
     * @return {@link User}
     */
    User getByEmail(String email);

    /**
     * @return 用户信息
     */
    List<User> getByAccount(String username, String email, String mobile);

}

