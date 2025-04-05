package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.query.MenuQuery;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.domain.repository.MenuRepository;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.converter.MenuConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.MenuMapper;
import top.ticho.rainbow.infrastructure.persistence.po.MenuPO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;

import java.util.List;
import java.util.Objects;

/**
 * 菜单信息 repository实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
@RequiredArgsConstructor
public class MenuRepositoryImpl extends TiRepositoryImpl<MenuMapper, MenuPO> implements MenuRepository {
    private final MenuConverter menuConverter;

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'")
    public boolean save(Menu menu) {
        MenuPO menuPO = menuConverter.toPo(menu);
        return super.save(menuPO);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'")
    public boolean remove(Long id) {
        return removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'")
    public boolean modify(Menu menu) {
        MenuPO menuPO = menuConverter.toPo(menu);
        return super.updateById(menuPO);
    }

    @Override
    public Menu find(Long id) {
        return menuConverter.toEntity(super.getById(id));
    }

    @Override
    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:menu:list'", sync = true)
    public List<Menu> cacheList() {
        return menuConverter.toEntitys(super.list());
    }

    @Override
    public List<Menu> list(MenuQuery query) {
        LambdaQueryWrapper<MenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(query.getId()), MenuPO::getId, query.getId());
        wrapper.eq(Objects.nonNull(query.getParentId()), MenuPO::getParentId, query.getParentId());
        wrapper.eq(StrUtil.isNotBlank(query.getStructure()), MenuPO::getStructure, query.getStructure());
        wrapper.in(CollUtil.isNotEmpty(query.getTypes()), MenuPO::getType, query.getTypes());
        wrapper.eq(StrUtil.isNotBlank(query.getName()), MenuPO::getName, query.getName());
        wrapper.eq(StrUtil.isNotBlank(query.getPath()), MenuPO::getPath, query.getPath());
        wrapper.eq(StrUtil.isNotBlank(query.getComponent()), MenuPO::getComponent, query.getComponent());
        wrapper.eq(StrUtil.isNotBlank(query.getRedirect()), MenuPO::getRedirect, query.getRedirect());
        wrapper.eq(Objects.nonNull(query.getExtFlag()), MenuPO::getExtFlag, query.getExtFlag());
        wrapper.eq(Objects.nonNull(query.getKeepAlive()), MenuPO::getKeepAlive, query.getKeepAlive());
        wrapper.eq(Objects.nonNull(query.getInvisible()), MenuPO::getInvisible, query.getInvisible());
        wrapper.eq(Objects.nonNull(query.getClosable()), MenuPO::getClosable, query.getClosable());
        wrapper.eq(StrUtil.isNotBlank(query.getIcon()), MenuPO::getIcon, query.getIcon());
        wrapper.eq(Objects.nonNull(query.getSort()), MenuPO::getSort, query.getSort());
        wrapper.eq(Objects.nonNull(query.getStatus()), MenuPO::getStatus, query.getStatus());
        wrapper.eq(StrUtil.isNotBlank(query.getRemark()), MenuPO::getRemark, query.getRemark());
        wrapper.eq(Objects.nonNull(query.getVersion()), MenuPO::getVersion, query.getVersion());
        wrapper.eq(StrUtil.isNotBlank(query.getCreateBy()), MenuPO::getCreateBy, query.getCreateBy());
        wrapper.eq(Objects.nonNull(query.getCreateTime()), MenuPO::getCreateTime, query.getCreateTime());
        wrapper.eq(StrUtil.isNotBlank(query.getUpdateBy()), MenuPO::getUpdateBy, query.getUpdateBy());
        wrapper.eq(Objects.nonNull(query.getUpdateTime()), MenuPO::getUpdateTime, query.getUpdateTime());
        wrapper.orderByAsc(MenuPO::getParentId);
        wrapper.orderByAsc(MenuPO::getSort);
        return menuConverter.toEntitys(list(wrapper));
    }

    @Override
    public Menu getByTypesAndPath(List<Integer> types, String path, Long excludeId) {
        if (CollUtil.isEmpty(types) || StrUtil.isBlank(path)) {
            return null;
        }
        LambdaQueryWrapper<MenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MenuPO::getType, types);
        wrapper.eq(MenuPO::getPath, path);
        wrapper.ne(Objects.nonNull(excludeId), MenuPO::getId, excludeId);
        wrapper.last("limit 1");
        return menuConverter.toEntity(getOne(wrapper));
    }

    public Menu getByTypesAndComNameExcludeId(List<Integer> types, String componentName, Long excludeId) {
        if (Objects.isNull(types) || StrUtil.isBlank(componentName)) {
            return null;
        }
        LambdaQueryWrapper<MenuPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(MenuPO::getType, types);
        wrapper.eq(MenuPO::getComponentName, componentName);
        wrapper.ne(Objects.nonNull(excludeId), MenuPO::getId, excludeId);
        wrapper.last("limit 1");
        return menuConverter.toEntity(getOne(wrapper));
    }

}
