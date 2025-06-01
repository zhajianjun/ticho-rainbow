package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.User;

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

    boolean remove(User user);

    boolean modify(User user);

    boolean modifyBatch(List<User> users);

    User find(Long id);

    List<User> list(List<Long> ids);

    User findCacheByUsername(String username);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findByAccount(String username, String email, String mobile);

}

