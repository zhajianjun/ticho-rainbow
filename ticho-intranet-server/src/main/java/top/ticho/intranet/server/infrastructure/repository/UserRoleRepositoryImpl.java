package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.intranet.server.domain.repository.UserRoleRepository;
import top.ticho.intranet.server.infrastructure.entity.UserRole;
import top.ticho.intranet.server.infrastructure.mapper.UserRoleMapper;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户角色关联关系 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class UserRoleRepositoryImpl extends RootServiceImpl<UserRoleMapper, UserRole> implements UserRoleRepository {

    // @Autowired
    // private RedisUtil<String, String> redisUtil;

    @PostConstruct
    public void init() {
        list();
    }

    public List<UserRole> list() {
        // @formatter:off
        // if (redisUtil.exists(RedisConst.USER_ROLE_LIST_KEY)) {
        //     Map<String, String> str = redisUtil.hGetAll(RedisConst.USER_ROLE_LIST_KEY);
        //     return str.values()
        //         .stream()
        //         .map(x-> JsonUtil.toList(x, UserRole.class))
        //         .flatMap(Collection::stream)
        //         .collect(Collectors.toList());
        // }
        List<UserRole> list = super.list();
        saveCache(list);
        return list;
    }

    @Override
    public boolean saveBatch(Collection<UserRole> entityList) {
        // @formatter:off
        boolean saveBatch = super.saveBatch(entityList);
        if (!saveBatch) {
            return false;
        }
        saveCache(entityList);
        return true;
        // @formatter:on
    }

    private void saveCache(Collection<UserRole> entityList) {
        // @formatter:off
        Map<String, List<UserRole>> collect = entityList
            .stream()
            .collect(Collectors.groupingBy(x-> x.getUserId().toString(), Collectors.toList()));
        Map<String, String> result = collect
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, x -> JsonUtil.toJsonString(x.getValue())));
        // redisUtil.hPutAll(RedisConst.USER_ROLE_LIST_KEY, result);
        // @formatter:on
    }

    @Override
    public List<UserRole> listByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return Collections.emptyList();
        }
        // String userRoleStr = redisUtil.hGet(RedisConst.USER_ROLE_LIST_KEY, userId.toString());
        // if (StrUtil.isNotBlank(userRoleStr)) {
        //     return JsonUtil.toList(userRoleStr, UserRole.class);
        // }
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> list = list(wrapper);
        // redisUtil.hPut(RedisConst.USER_ROLE_LIST_KEY, userId.toString(), JsonUtil.toJsonString(list));
        return list;
    }

    @Override
    public List<UserRole> listByUserIds(Collection<Long> userIds) {
        // @formatter:off
        if (CollUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        // if (redisUtil.exists(RedisConst.USER_ROLE_LIST_KEY)) {
        //     List<String> userIdStrs = userIds.stream().map(Object::toString).collect(Collectors.toList());
        //     List<String> userRoleStr = redisUtil.hMultiGet(RedisConst.USER_ROLE_LIST_KEY, userIdStrs);
        //     return userRoleStr
        //         .stream()
        //         .map(x-> JsonUtil.toList(x, UserRole.class))
        //         .flatMap(Collection::stream)
        //         .collect(Collectors.toList());
        // }
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.in(UserRole::getUserId, userIds);
        List<UserRole> list = list(wrapper);
        Map<String, List<UserRole>> collect = list
            .stream()
            .collect(Collectors.groupingBy(x-> x.getUserId().toString()));
        // redisUtil.hPutAll(RedisConst.USER_ROLE_LIST_KEY, collect);
        return list;
    }

    @Override
    public boolean existsByRoleIds(Collection<Long> roleIds) {
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.in(UserRole::getRoleId, roleIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    public boolean removeByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            return false;
        }
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);
        boolean remove = remove(wrapper);
        // redisUtil.hDelete(RedisConst.USER_ROLE_LIST_KEY, userId.toString());
        return remove;
    }

}
