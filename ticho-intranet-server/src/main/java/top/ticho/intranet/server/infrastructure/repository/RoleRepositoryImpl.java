package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.RoleRepository;
import top.ticho.intranet.server.infrastructure.core.constant.CacheConst;
import top.ticho.intranet.server.infrastructure.entity.Role;
import top.ticho.intranet.server.infrastructure.mapper.RoleMapper;
import top.ticho.intranet.server.interfaces.query.RoleQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class RoleRepositoryImpl extends RootServiceImpl<RoleMapper, Role> implements RoleRepository {
    public static final String ROLE_LIST = CacheConst.ROLE_LIST;

    @Override
    @Cacheable(value = CacheConst.COMMON, key = "#root.target.ROLE_LIST", sync = true)
    public List<Role> cacheList() {
        return super.list();
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "#root.target.ROLE_LIST")
    public boolean save(Role role) {
        return super.save(role);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "#root.target.ROLE_LIST")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "#root.target.ROLE_LIST")
    public boolean updateById(Role role) {
        return super.updateById(role);
    }

    @Override
    public List<Role> list(RoleQuery query) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), Role::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getCode()), Role::getCode, query.getCode());
        wrapper.like(StrUtil.isNotBlank(query.getName()), Role::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getStatus()), Role::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), Role::getRemark, query.getRemark());
        wrapper.orderByDesc(Role::getId);
        return list(wrapper);
    }

    @Override
    public List<Role> listByCodes(List<String> codes) {
        // @formatter:off
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }
        RoleRepositoryImpl bean = SpringUtil.getBean(getClass());
        List<Role> list = bean.cacheList();
        return list
            .stream()
            .filter(x-> codes.contains(x.getCode()))
            .collect(Collectors.toList());
        // @formatter:on
    }

    @Override
    public Role getByCodeExcludeId(String code, Long excludeId) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getCode, code);
        wrapper.ne(Objects.nonNull(excludeId), Role::getId, excludeId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public List<Role> listByIds(Collection<? extends Serializable> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        RoleRepositoryImpl bean = SpringUtil.getBean(getClass());
        List<Role> list = bean.cacheList();
        return list
            .stream()
            .filter(x-> ids.contains(x.getId()))
            .collect(Collectors.toList());
    }

}
