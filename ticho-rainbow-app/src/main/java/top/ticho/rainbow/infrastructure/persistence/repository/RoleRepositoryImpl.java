package top.ticho.rainbow.infrastructure.persistence.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.interfaces.dto.query.RoleQuery;
import top.ticho.rainbow.interfaces.dto.response.RoleDTO;
import top.ticho.rainbow.application.repository.RoleAppRepository;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.infrastructure.persistence.converter.RoleConverter;
import top.ticho.rainbow.infrastructure.persistence.mapper.RoleMapper;
import top.ticho.rainbow.infrastructure.persistence.po.RolePO;
import top.ticho.starter.datasource.service.impl.TiRepositoryImpl;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;

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
@Service
@RequiredArgsConstructor
public class RoleRepositoryImpl extends TiRepositoryImpl<RoleMapper, RolePO> implements RoleRepository, RoleAppRepository {
    private final RoleConverter roleConverter;

    @Override
    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:role:list'", sync = true)
    public List<Role> cacheList() {
        return roleConverter.toEntity(super.list());
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:role:list'")
    public boolean save(Role role) {
        RolePO rolePO = roleConverter.toPo(role);
        return super.save(rolePO);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:role:list'")
    public boolean remove(Long id) {
        return super.removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:role:list'")
    public boolean modify(Role role) {
        RolePO rolePO = roleConverter.toPo(role);
        return super.updateById(rolePO);
    }

    @Override
    @CacheEvict(value = CacheConst.COMMON, key = "'ticho-rainbow:role:list'")
    public boolean modifyBatch(List<Role> roles) {
        return super.updateBatchById(roleConverter.toPo(roles));
    }

    @Override
    public Role find(Long id) {
        RolePO rolePO = super.getById(id);
        return roleConverter.toEntity(rolePO);
    }


    @Override
    public TiPageResult<RoleDTO> page(RoleQuery query) {
        LambdaQueryWrapper<RolePO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(query.getIds()), RolePO::getId, query.getIds());
        wrapper.eq(Objects.nonNull(query.getId()), RolePO::getId, query.getId());
        wrapper.like(StrUtil.isNotBlank(query.getCode()), RolePO::getCode, query.getCode());
        wrapper.like(StrUtil.isNotBlank(query.getName()), RolePO::getName, query.getName());
        wrapper.eq(Objects.nonNull(query.getStatus()), RolePO::getStatus, query.getStatus());
        wrapper.like(StrUtil.isNotBlank(query.getRemark()), RolePO::getRemark, query.getRemark());
        wrapper.orderByDesc(RolePO::getId);
        return TiPageUtil.page(() -> list(wrapper), query, roleConverter::toDTO);
    }

    @Override
    public List<RoleDTO> all() {
        return list()
            .stream()
            .map(roleConverter::toDTO)
            .collect(Collectors.toList());
    }


    @Override
    public List<Role> listByCodes(List<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }
        RoleRepositoryImpl bean = SpringUtil.getBean(getClass());
        List<Role> list = bean.cacheList();
        return list
            .stream()
            .filter(x -> codes.contains(x.getCode()))
            .collect(Collectors.toList());
    }

    @Override
    public Role getGuestRole() {
        return listByCodes(Collections.singletonList(CommConst.GUEST_ROLE_CODE)).stream().findFirst().orElse(null);
    }

    @Override
    public Role getByCodeExcludeId(String code, Long excludeId) {
        LambdaQueryWrapper<RolePO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RolePO::getCode, code);
        wrapper.ne(Objects.nonNull(excludeId), RolePO::getId, excludeId);
        wrapper.last("limit 1");
        return roleConverter.toEntity(getOne(wrapper));
    }

    @Override
    public List<Role> list(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        RoleRepositoryImpl bean = SpringUtil.getBean(getClass());
        List<Role> list = bean.cacheList();
        return list
            .stream()
            .filter(x -> ids.contains(x.getId()))
            .collect(Collectors.toList());
    }

}
