package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.boot.datasource.service.impl.RootServiceImpl;
import top.ticho.rainbow.domain.repository.MenuRepository;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.entity.Menu;
import top.ticho.rainbow.infrastructure.mapper.MenuMapper;
import top.ticho.rainbow.interfaces.query.MenuQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 菜单信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@Service
public class MenuRepositoryImpl extends RootServiceImpl<MenuMapper, Menu> implements MenuRepository {

    @Override
    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'", sync = true)
    public List<Menu> cacheList() {
        return super.list();
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'")
    public boolean save(Menu menu) {
        return super.save(menu);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'")
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'")
    public boolean updateById(Menu menu) {
        return super.updateById(menu);
    }


    @Override
    public List<Menu> list(MenuQuery query) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), Menu::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getParentId()), Menu::getParentId, query.getParentId());
        wrapper.eq(StrUtil.isNotBlank(query.getStructure()), Menu::getStructure, query.getStructure());
        wrapper.in(CollUtil.isNotEmpty(query.getTypes()), Menu::getType, query.getTypes());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), Menu::getName, query.getName());
        wrapper.eq(StrUtil.isNotBlank(query.getPath()), Menu::getPath, query.getPath());
        wrapper.eq(StrUtil.isNotBlank(query.getComponent()), Menu::getComponent, query.getComponent());
        wrapper.eq(StrUtil.isNotBlank(query.getRedirect()), Menu::getRedirect, query.getRedirect());
        wrapper.eq(Objects.nonNull(query.getExtFlag()), Menu::getExtFlag, query.getExtFlag());
        wrapper.eq(Objects.nonNull(query.getKeepAlive()), Menu::getKeepAlive, query.getKeepAlive());
        wrapper.eq(Objects.nonNull(query.getInvisible()), Menu::getInvisible, query.getInvisible());
        wrapper.eq(Objects.nonNull(query.getClosable()), Menu::getClosable, query.getClosable());
        wrapper.eq(StrUtil.isNotBlank(query.getIcon()), Menu::getIcon, query.getIcon());
        wrapper.eq(Objects.nonNull(query.getSort()), Menu::getSort, query.getSort());
        wrapper.eq(Objects.nonNull(query.getStatus()), Menu::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), Menu::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), Menu::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), Menu::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), Menu::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), Menu::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), Menu::getUpdateTime, query.getUpdateTime());
        wrapper.orderByAsc(Menu::getParentId);
        wrapper.orderByAsc(Menu::getSort);
        return list(wrapper);
    }

    @Override
    public Menu getByTypesAndPath(List<Integer> types, String path, Long excludeId) {
        if (CollUtil.isEmpty(types) || StrUtil.isBlank(path)) {
            return null;
        }
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Menu::getType, types);
        wrapper.eq(Menu::getPath, path);
        wrapper.ne(Objects.nonNull(excludeId), Menu::getId, excludeId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    public Menu getByTypesAndComNameExcludeId(List<Integer> types, String componentName, Long excludeId) {
        if (Objects.isNull(types) || StrUtil.isBlank(componentName)) {
            return null;
        }
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        wrapper.in(Menu::getType, types);
        wrapper.eq(Menu::getComponentName, componentName);
        wrapper.ne(Objects.nonNull(excludeId), Menu::getId, excludeId);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

}
