package top.ticho.rainbow.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import top.ticho.rainbow.application.repository.SettingAppRepository;
import top.ticho.rainbow.domain.entity.Setting;
import top.ticho.rainbow.domain.repository.SettingRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.persistence.converter.SettingConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.SettingMapper;
import top.ticho.rainbow.infrastructure.persistence.po.SettingPO;
import top.ticho.rainbow.interfaces.dto.SettingDTO;
import top.ticho.rainbow.interfaces.query.SettingQuery;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.tool.core.TiCollUtil;
import top.ticho.tool.core.TiStrUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配置信息 repository实现
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@RequiredArgsConstructor
@Repository
public class SettingRepositoryImpl extends TiRepositoryImpl<SettingMapper, SettingPO> implements SettingRepository, SettingAppRepository {
    private final SettingConverter settingConverter;

    public List<Setting> cacheList() {
        return settingConverter.toEntity(super.list());
    }

    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:map'", sync = true)
    @Override
    public Map<String, String> cacheMap() {
        List<SettingPO> list = super.list();
        return list
            .stream()
            .collect(Collectors.toMap(SettingPO::getKey, SettingPO::getValue));
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:map'")
    public boolean save(Setting setting) {
        return super.save(settingConverter.toPO(setting));
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:map'")
    public boolean remove(Long id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:map'")
    public boolean modify(Setting setting) {
        return super.updateById(settingConverter.toPO(setting));
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:map'")
    public boolean modifyBatch(List<Setting> settings) {
        return super.updateBatchById(settingConverter.toPO(settings));
    }

    @Override
    public Setting find(Long id) {
        return settingConverter.toEntity(super.getById(id));
    }

    @Override
    public List<Setting> list(List<Long> ids) {
        return settingConverter.toEntity(super.listByIds(ids));
    }

    @Override
    public Setting findByKey(String key) {
        if (TiStrUtil.isBlank(key)) {
            return null;
        }
        LambdaQueryWrapper<SettingPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SettingPO::getKey, key);
        wrapper.last("limit 1");
        return settingConverter.toEntity(getOne(wrapper));
    }

    @Override
    public TiPageResult<SettingDTO> page(SettingQuery query) {
        query.checkPage();
        LambdaQueryWrapper<SettingPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(TiCollUtil.isNotEmpty(query.getIds()), SettingPO::getId, query.getIds());
        wrapper.eq(TiStrUtil.isNotBlank(query.getKey()), SettingPO::getKey, query.getKey());
        wrapper.eq(TiStrUtil.isNotBlank(query.getValue()), SettingPO::getValue, query.getValue());
        wrapper.eq(TiStrUtil.isNotBlank(query.getRemark()), SettingPO::getRemark, query.getRemark());
        wrapper.orderByAsc(SettingPO::getSort);
        wrapper.orderByDesc(SettingPO::getId);
        Page<SettingPO> page = new Page<>(query.getPageNum(), query.getPageSize(), query.getCount());
        page(page, wrapper);
        return TiPageUtil.of(page, settingConverter::toDTO);
    }

}
