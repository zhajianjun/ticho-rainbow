package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.converter.UserConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.UserMapper;
import top.ticho.rainbow.infrastructure.persistence.po.UserPO;
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
@Service
@RequiredArgsConstructor
public class UserRepositoryImpl extends TiRepositoryImpl<UserMapper, UserPO> implements UserRepository {
    private final UserConverter userConverter;

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#user.username")
    public boolean save(User user) {
        TiAssert.isNotBlank(user.getUsername(), "用户名不能为空");
        UserPO userPO = userConverter.toPo(user);
        return save(userPO);
    }

    @Override
    @Transactional
    public boolean saveBatch(List<User> users) {
        List<UserPO> userPOs = userConverter.toPos(users);
        return saveBatch(userPOs);
    }

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#user.username")
    public boolean modify(User user) {
        // 为了保证缓存，用户名不能为空
        TiAssert.isNotBlank(user.getUsername(), "用户名不能为空");
        UserPO userPO = userConverter.toPo(user);
        return super.updateById(userPO);
    }

    @Override
    public User find(Long id) {
        UserPO userPO = getById(id);
        return userConverter.toEntity(userPO);
    }

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
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPO::getUsername, username);
        return userConverter.toEntity(getOne(wrapper));
    }

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#username")
    public boolean removeByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return false;
        }
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPO::getUsername, username);
        return remove(wrapper);
    }

    @Override
    public Integer modifyStatus(Collection<String> usernames, Integer status, Collection<Integer> eqDbStatus, Collection<Integer> neDbStatus) {
        if (CollUtil.isEmpty(usernames)) {
            return 0;
        }
        LambdaUpdateWrapper<UserPO> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(UserPO::getUsername, usernames);
        wrapper.in(CollUtil.isNotEmpty(eqDbStatus), UserPO::getStatus, eqDbStatus);
        wrapper.notIn(CollUtil.isNotEmpty(neDbStatus), UserPO::getStatus, neDbStatus);
        wrapper.set(UserPO::getStatus, status);
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
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPO::getEmail, email);
        return userConverter.toEntity(getOne(wrapper));
    }

    @Override
    public List<User> list(UserQuery query) {
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), UserPO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), UserPO::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getUsername()), UserPO::getUsername, query.getUsername());
        wrapper.like(StrUtil.isNotBlank(query.getNickname()), UserPO::getNickname, query.getNickname());
        wrapper.like(StrUtil.isNotBlank(query.getRealname()), UserPO::getRealname, query.getRealname());
        wrapper.eq(StrUtil.isNotBlank(query.getIdcard()), UserPO::getIdcard, query.getIdcard());
        wrapper.eq(Objects.nonNull(query.getSex()), UserPO::getSex, query.getSex());
        wrapper.eq(Objects.nonNull(query.getAge()), UserPO::getAge, query.getAge());
        wrapper.eq(Objects.nonNull(query.getBirthday()), UserPO::getBirthday, query.getBirthday());
        wrapper.like(StrUtil.isNotBlank(query.getAddress()), UserPO::getAddress, query.getAddress());
        wrapper.like(StrUtil.isNotBlank(query.getEducation()), UserPO::getEducation, query.getEducation());
        wrapper.like(StrUtil.isNotBlank(query.getEmail()), UserPO::getEmail, query.getEmail());
        wrapper.like(StrUtil.isNotBlank(query.getQq()), UserPO::getQq, query.getQq());
        wrapper.like(StrUtil.isNotBlank(query.getWechat()), UserPO::getWechat, query.getWechat());
        wrapper.like(StrUtil.isNotBlank(query.getMobile()), UserPO::getMobile, query.getMobile());
        wrapper.eq(StrUtil.isNotBlank(query.getLastIp()), UserPO::getLastIp, query.getLastIp());
        wrapper.eq(Objects.nonNull(query.getLastTime()), UserPO::getLastTime, query.getLastTime());
        wrapper.eq(Objects.nonNull(query.getStatus()), UserPO::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), UserPO::getRemark, query.getRemark());
        wrapper.orderByDesc(UserPO::getId);
        return userConverter.toEntitys(list(wrapper));
    }

    @Override
    public List<User> getByAccount(String username, String email, String mobile) {
        if (StrUtil.isAllBlank(username, email, mobile)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper
            .and(x ->
                x.eq(StrUtil.isNotBlank(username), UserPO::getUsername, username)
                    .or()
                    .eq(StrUtil.isNotBlank(email), UserPO::getEmail, email)
                    .or()
                    .eq(StrUtil.isNotBlank(mobile), UserPO::getMobile, mobile)
            );
        return userConverter.toEntitys(list(wrapper));
    }

}
