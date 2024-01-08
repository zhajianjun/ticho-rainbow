package top.ticho.intranet.server.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.intranet.server.domain.repository.RoleMenuRepository;
import top.ticho.intranet.server.infrastructure.entity.RoleMenu;
import top.ticho.intranet.server.infrastructure.mapper.RoleMenuMapper;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色菜单关联关系 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class RoleMenuRepositoryImpl extends RootServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuRepository {

    // @Autowired
    // private RedisUtil<String, String> redisUtil;

    @PostConstruct
    public void init() {
        list();
    }

    public List<RoleMenu> list() {
        // @formatter:off
        // if (redisUtil.exists(RedisConst.ROLE_MENU_LIST_KEY)) {
        //     Map<String, String> str = redisUtil.hGetAll(RedisConst.ROLE_MENU_LIST_KEY);
        //     return str.values()
        //         .stream()
        //         .map(x-> JsonUtil.toList(x, RoleMenu.class))
        //         .flatMap(Collection::stream)
        //         .collect(Collectors.toList());
        // }
        List<RoleMenu> list = super.list();
        saveCache(list);
        return list;
        // @formatter:on
    }

    @Override
    public boolean saveBatch(Collection<RoleMenu> entityList) {
        boolean saveBatch = super.saveBatch(entityList);
        if (!saveBatch) {
            return false;
        }
        saveCache(entityList);
        return false;
        // @formatter:on
    }

    public void saveCache(Collection<RoleMenu> entityList) {
        // @formatter:off
        Map<String, List<RoleMenu>> collect = entityList
            .stream()
            .collect(Collectors.groupingBy(x-> x.getRoleId().toString(), Collectors.toList()));
        Map<String, String> result = collect
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, x -> JsonUtil.toJsonString(x.getValue())));
        // redisUtil.hPutAll(RedisConst.ROLE_MENU_LIST_KEY, result);
        // @formatter:on
    }

    @Override
    public List<RoleMenu> listByRoleIds(List<Long> roleIds) {
        // @formatter:off
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<String> roleStrs = roleIds.stream().map(Object::toString).collect(Collectors.toList());
        // boolean exists = redisUtil.exists(RedisConst.ROLE_MENU_LIST_KEY);
        // if (!exists) {
        //     List<String> roleMenus = redisUtil.hMultiGet(RedisConst.ROLE_MENU_LIST_KEY, roleStrs);
        //     return roleMenus
        //         .stream()
        //         .map(x-> JsonUtil.toList(x, RoleMenu.class))
        //         .flatMap(Collection::stream)
        //         .collect(Collectors.toList());
        // }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenu::getRoleId, roleIds);
        return list(wrapper);
        // @formatter:on
    }

    @Override
    public boolean existsByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return false;
        }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenu::getRoleId, roleIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    public boolean existsByMenuIds(Collection<Long> menuIds) {
        if (CollUtil.isEmpty(menuIds)) {
            return false;
        }
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(RoleMenu::getMenuId, menuIds);
        wrapper.last("limit 1");
        return !list(wrapper).isEmpty();
    }

    @Override
    public boolean removeByRoleIdAndMenuIds(Long roleId, Collection<Long> menuIds) {
        if (Objects.isNull(roleId) || CollUtil.isEmpty(menuIds)) {
            return false;
        }
        List<RoleMenu> roleMenus = listByRoleIds(Collections.singletonList(roleId));
        LambdaQueryWrapper<RoleMenu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        wrapper.in(RoleMenu::getMenuId, menuIds);
        boolean remove = remove(wrapper);
        roleMenus.removeIf(x -> menuIds.contains(x.getMenuId()));
        // redisUtil.hPut(RedisConst.ROLE_MENU_LIST_KEY, roleId.toString(), JsonUtil.toJsonString(roleMenus));
        return remove;
    }


}
