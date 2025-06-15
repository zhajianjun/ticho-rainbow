package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.repository.UserAppRepository;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.converter.UserConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.UserMapper;
import top.ticho.rainbow.infrastructure.persistence.po.UserPO;
import top.ticho.rainbow.interfaces.query.UserQuery;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;
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
public class UserRepositoryImpl extends TiRepositoryImpl<UserMapper, UserPO> implements UserRepository, UserAppRepository {
    private final UserConverter userConverter;

    @Override
    public boolean save(User user) {
        UserPO userPO = userConverter.toPo(user);
        return save(userPO);
    }

    @Override
    @Transactional
    public boolean saveBatch(List<User> users) {
        List<UserPO> userPOs = userConverter.toPo(users);
        return super.saveBatch(userPOs);
    }

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#user.username")
    public boolean remove(User user) {
        return super.removeById(user.getId());
    }

    @Override
    @CacheEvict(value = CacheConst.USER_INFO, key = "#user.username")
    public boolean modify(User user) {
        UserPO userPO = userConverter.toPo(user);
        return super.updateById(userPO);
    }

    @Override
    @Transactional
    public boolean modifyBatch(List<User> users) {
        List<UserPO> userPOs = userConverter.toPo(users);
        return super.updateBatchById(userPOs);
    }

    @Override
    public User find(Long id) {
        UserPO userPO = super.getById(id);
        return userConverter.toEntity(userPO);
    }

    @Override
    public List<User> list(List<Long> ids) {
        List<UserPO> pos = super.listByIds(ids);
        return userConverter.toEntity(pos);
    }

    @Override
    @Cacheable(value = CacheConst.USER_INFO, key = "#username")
    public User findCacheByUsername(String username) {
        return findByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPO::getUsername, username);
        return userConverter.toEntity(getOne(wrapper));
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
    public User findByEmail(String email) {
        if (StrUtil.isBlank(email)) {
            return null;
        }
        LambdaQueryWrapper<UserPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserPO::getEmail, email);
        return userConverter.toEntity(getOne(wrapper));
    }

    @Override
    public TiPageResult<UserDTO> page(UserQuery query) {
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
        return TiPageUtil.page(() -> list(wrapper), query, userConverter::toDTO);
    }

    @Override
    public List<User> findByAccount(String username, String email, String mobile) {
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
        return userConverter.toEntity(list(wrapper));
    }

}
