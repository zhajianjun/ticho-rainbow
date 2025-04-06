package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
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
