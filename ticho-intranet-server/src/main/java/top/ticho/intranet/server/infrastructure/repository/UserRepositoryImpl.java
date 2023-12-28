package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ticho.boot.datasource.service.impl.RootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.domain.repository.UserRepository;
import top.ticho.intranet.server.infrastructure.entity.User;
import top.ticho.intranet.server.infrastructure.mapper.UserMapper;
import top.ticho.intranet.server.interfaces.query.UserQuery;

import java.util.List;
import java.util.Objects;

/**
 * 用户信息 repository实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Slf4j
@Service
public class UserRepositoryImpl extends RootServiceImpl<UserMapper, User> implements UserRepository {

    @Override
    public List<User> list(UserQuery query) {
        // @formatter:off
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getUsername()), User::getUsername, query.getUsername());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), User::getRemark, query.getRemark());
        return list(wrapper);
        // @formatter:on
    }

    @Override
    public User getByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

}
