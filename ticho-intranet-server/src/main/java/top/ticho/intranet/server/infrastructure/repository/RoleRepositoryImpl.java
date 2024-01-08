package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.intranet.server.domain.repository.RoleRepository;
import top.ticho.intranet.server.infrastructure.core.constant.RedisConst;
import top.ticho.intranet.server.infrastructure.core.prop.CacheProperty;
import top.ticho.intranet.server.infrastructure.entity.Role;
import top.ticho.intranet.server.infrastructure.mapper.RoleMapper;
import top.ticho.intranet.server.interfaces.query.RoleQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 角色信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class RoleRepositoryImpl extends RootServiceImpl<RoleMapper, Role> implements RoleRepository {

    // @Autowired
    // private RedisUtil<String, String> redisUtil;

    @Autowired
    private CacheProperty cacheProperty;

    @Override
    public List<Role> list() {
        // @formatter:off
        // boolean exists = redisUtil.exists(RedisConst.ROLE_LIST_KEY);
        // if (exists) {
        //     String vGet = redisUtil.vGet(RedisConst.ROLE_LIST_KEY);
        //     return JsonUtil.toList(vGet, Role.class);
        // }
        List<Role> list = super.list();
        // redisUtil.vSet(RedisConst.ROLE_LIST_KEY, JsonUtil.toJsonString(list), cacheProperty.getRoleExpire(), TimeUnit.SECONDS);
        return list;
        // @formatter:on
    }


    @Override
    public boolean removeById(Serializable id) {
        // if (remove) {
        //     redisUtil.delete(RedisConst.ROLE_LIST_KEY);
        // }
        return super.removeById(id);
    }

    @Override
    public boolean updateById(Role role) {
        // if (update) {
        //     redisUtil.delete(RedisConst.ROLE_LIST_KEY);
        // }
        return super.updateById(role);
    }

    @Override
    public List<Role> list(RoleQuery query) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), Role::getId, query.getId());
        wrapper.eq(StrUtil.isNotBlank(query.getCode()), Role::getCode, query.getCode());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), Role::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getStatus()), Role::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), Role::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), Role::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), Role::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), Role::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), Role::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), Role::getUpdateTime, query.getUpdateTime());
        wrapper.eq(Objects.nonNull(query.getIsDelete()), Role::getIsDelete, query.getIsDelete());
        return list(wrapper);
    }

    @Override
    public List<Role> listByCodes(List<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }
        List<Role> list = list();
        list.removeIf(x -> {
            boolean contains = codes.contains(x.getCode());
            return !contains;
        });
        return list;
    }

    @Override
    public List<Role> listByIds(Collection<? extends Serializable> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<Role> list = list();
        list.removeIf(x -> !ids.contains(x.getId()));
        return list;
    }

}
