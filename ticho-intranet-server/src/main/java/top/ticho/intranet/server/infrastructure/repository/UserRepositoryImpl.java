package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.UserRepository;
import top.ticho.intranet.server.infrastructure.entity.User;
import top.ticho.intranet.server.infrastructure.mapper.UserMapper;
import top.ticho.intranet.server.interfaces.query.UserAccountQuery;
import top.ticho.intranet.server.interfaces.query.UserQuery;

import java.util.List;
import java.util.Objects;

/**
 * 用户信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class UserRepositoryImpl extends RootServiceImpl<UserMapper, User> implements UserRepository {

    public User getByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = getUserLambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, username);
        return getOne(queryWrapper);
    }

    private LambdaQueryWrapper<User> getUserLambdaQueryWrapper() {
        return Wrappers.lambdaQuery();
    }

    @Override
    public List<User> list(UserQuery query) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), User::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getUsername()), User::getUsername, query.getUsername());
        wrapper.like(StrUtil.isNotBlank(query.getNickname()), User::getNickname, query.getNickname());
        wrapper.like(StrUtil.isNotBlank(query.getRealname()), User::getRealname, query.getRealname());
        wrapper.eq(StrUtil.isNotBlank(query.getIdcard()), User::getIdcard, query.getIdcard());
        wrapper.eq(Objects.nonNull(query.getSex()), User::getSex, query.getSex());
        wrapper.eq(Objects.nonNull(query.getAge()), User::getAge, query.getAge());
        wrapper.eq(Objects.nonNull(query.getBirthday()), User::getBirthday, query.getBirthday());
        wrapper.like(StrUtil.isNotBlank(query.getAddress()), User::getAddress, query.getAddress());
        wrapper.like(StrUtil.isNotBlank(query.getEducation()), User::getEducation, query.getEducation());
        wrapper.like(StrUtil.isNotBlank(query.getEmail()), User::getEmail, query.getEmail());
        wrapper.like(Objects.nonNull(query.getQq()), User::getQq, query.getQq());
        wrapper.like(StrUtil.isNotBlank(query.getWechat()), User::getWechat, query.getWechat());
        wrapper.like(StrUtil.isNotBlank(query.getMobile()), User::getMobile, query.getMobile());
        wrapper.eq(StrUtil.isNotBlank(query.getLastIp()), User::getLastIp, query.getLastIp());
        wrapper.eq(Objects.nonNull(query.getLastTime()), User::getLastTime, query.getLastTime());
        wrapper.eq(Objects.nonNull(query.getStatus()), User::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), User::getRemark, query.getRemark());
        wrapper.orderByDesc(User::getId);
        return list(wrapper);
    }

    @Override
    public List<User> getByAccount(UserAccountQuery userAccountQuery) {
        // @formatter:off
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        String username = userAccountQuery.getUsername();
        String email = userAccountQuery.getEmail();
        String mobile = userAccountQuery.getMobile();
        List<Integer> status = userAccountQuery.getStatus();
        wrapper
            .eq(CollUtil.isNotEmpty(status), User::getStatus, status)
            .and(x->
                x.eq(User::getUsername, username)
                 .or()
                 .eq(User::getEmail, email)
                 .or()
                 .eq(User::getMobile, mobile)
            );
        // @formatter:on
        return list(wrapper);
    }

}
