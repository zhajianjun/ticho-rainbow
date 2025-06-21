package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

import java.util.List;

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

    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:list'", sync = true)
    public List<Setting> cacheList() {
        return settingConverter.toEntity(super.list());
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:list'")
    public boolean save(Setting setting) {
        return super.save(settingConverter.toPO(setting));
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:list'")
    public boolean remove(Long id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:list'")
    public boolean modify(Setting setting) {
        return super.updateById(settingConverter.toPO(setting));
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:setting:list'")
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
        if (StrUtil.isBlank(key)) {
            return null;
        }
        LambdaQueryWrapper<SettingPO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SettingPO::getKey, key);
        wrapper.last("limit 1");
        return settingConverter.toEntity(getOne(wrapper));
    }

    @Override
    public TiPageResult<SettingDTO> page(SettingQuery settingQuery) {
        LambdaQueryWrapper<SettingPO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(settingQuery.getIds()), SettingPO::getId, settingQuery.getIds());
        wrapper.eq(StrUtil.isNotBlank(settingQuery.getKey()), SettingPO::getKey, settingQuery.getKey());
        wrapper.eq(StrUtil.isNotBlank(settingQuery.getValue()), SettingPO::getValue, settingQuery.getValue());
        wrapper.eq(StrUtil.isNotBlank(settingQuery.getRemark()), SettingPO::getRemark, settingQuery.getRemark());
        wrapper.orderByDesc(SettingPO::getId);
        return TiPageUtil.page(() -> list(wrapper), settingQuery, settingConverter::toDTO);
    }

}
