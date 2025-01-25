package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.entity.User;
import top.ticho.rainbow.infrastructure.mapper.UserMapper;
import top.ticho.rainbow.interfaces.query.UserAccountQuery;
import top.ticho.rainbow.interfaces.query.UserQuery;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiSpringUtil;

import java.util.Collection;
import java.util.Collections;
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
public class UserRepositoryImpl extends TiRepositoryImpl<UserMapper, User> implements UserRepository {

    @Override
    @Cacheable(value = CacheConst.USER_INFO, key = "#username")
    public User getCacheByUsername(String username) {
        return getByUsername(username);
    }

    @Override
    public User getByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#username")
    public boolean removeByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return false;
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        return remove(wrapper);
    }

    @Override
    public Integer updateStatus(Collection<String> usernames, Integer status, Collection<Integer> eqDbStatus, Collection<Integer> neDbStatus) {
        if (CollUtil.isEmpty(usernames)) {
            return 0;
        }
        LambdaUpdateWrapper<User> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(User::getUsername, usernames);
        wrapper.in(CollUtil.isNotEmpty(eqDbStatus), User::getStatus, eqDbStatus);
        wrapper.notIn(CollUtil.isNotEmpty(neDbStatus), User::getStatus, neDbStatus);
        wrapper.set(User::getStatus, status);
        int update = baseMapper.update(null, wrapper);
        clearCache(usernames);
        return update;
    }

    private void clearCache(Collection<String> usernames) {
        UserRepositoryImpl bean = TiSpringUtil.getBean(this.getClass());
        usernames.forEach(bean::clearCache);
    }

    @CacheEvict(value = CacheConst.USER_INFO, key = "#username")
    public void clearCache(String username) {
    }

    @Override
    @Cacheable(value = CacheConst.USER_INFO, unless = "#result == null", key = "#result==null ? '' : result.username")
    public User getByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(User::getEmail, email);
        return getOne(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#user.username")
    public boolean updateById(User user) {
        // 为了保证缓存，用户名不能为空
        TiAssert.isNotBlank(user.getUsername(), "用户名不能为空");
        return super.updateById(user);
    }

    @Override
    public List<User> list(UserQuery query) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), User::getId, query.getIds());
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
        wrapper.like(StrUtil.isNotBlank(query.getQq()), User::getQq, query.getQq());
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
        String username = userAccountQuery.getUsername();
        String email = userAccountQuery.getEmail();
        String mobile = userAccountQuery.getMobile();
        List<Integer> status = userAccountQuery.getStatus();
        if (StrUtil.isAllBlank(username, email, mobile)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper
            .eq(CollUtil.isNotEmpty(status), User::getStatus, status)
            .and(x ->
                x.eq(StrUtil.isNotBlank(username), User::getUsername, username)
                    .or()
                    .eq(StrUtil.isNotBlank(email), User::getEmail, email)
                    .or()
                    .eq(StrUtil.isNotBlank(mobile), User::getMobile, mobile)
            );
        return list(wrapper);
    }

}
