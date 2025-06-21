package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.Setting;

import java.util.List;

/**
 * 配置信息 repository接口
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
public interface SettingRepository {

    List<Setting> cacheList();

    boolean save(Setting setting);

    boolean remove(Long id);

    boolean modify(Setting setting);

    boolean modifyBatch(List<Setting> settings);

    Setting find(Long id);

    List<Setting> list(List<Long> ids);

    Setting findByKey(String key);

}

