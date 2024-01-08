package top.ticho.intranet.server.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.RoleService;
import top.ticho.intranet.server.domain.handle.UpmsHandle;
import top.ticho.intranet.server.domain.repository.RoleMenuRepository;
import top.ticho.intranet.server.domain.repository.RoleRepository;
import top.ticho.intranet.server.domain.repository.UserRoleRepository;
import top.ticho.intranet.server.infrastructure.entity.Role;
import top.ticho.intranet.server.infrastructure.entity.RoleMenu;
import top.ticho.intranet.server.interfaces.assembler.RoleAssembler;
import top.ticho.intranet.server.interfaces.dto.RoleDTO;
import top.ticho.intranet.server.interfaces.dto.RoleMenuDTO;
import top.ticho.intranet.server.interfaces.dto.RoleMenuDtlDTO;
import top.ticho.intranet.server.interfaces.query.RoleDtlQuery;
import top.ticho.intranet.server.interfaces.query.RoleQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
public class RoleServiceImpl extends UpmsHandle implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleDTO roleDTO) {
        Role role = RoleAssembler.INSTANCE.dtoToEntity(roleDTO);
        Assert.isTrue(roleRepository.save(role), BizErrCode.FAIL, "保存失败");
        List<Long> menuIds = roleDTO.getMenuIds();
        RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
        roleMenuDTO.setRoleId(role.getId());
        roleMenuDTO.setMenuIds(menuIds);
        bindMenu(roleMenuDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        List<Long> roleIds = Collections.singletonList(id);
        boolean userRoleExists = userRoleRepository.existsByRoleIds(roleIds);
        Assert.isTrue(!userRoleExists, BizErrCode.FAIL, "删除失败,请解绑所有的用户角色");
        boolean roleMenuExists = roleMenuRepository.existsByRoleIds(roleIds);
        Assert.isTrue(!roleMenuExists, BizErrCode.FAIL, "删除失败,请解绑所有的角色菜单");
        Assert.isTrue(roleRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(RoleDTO roleDTO) {
        Role role = RoleAssembler.INSTANCE.dtoToEntity(roleDTO);
        Assert.isTrue(roleRepository.updateById(role), BizErrCode.FAIL, "修改失败");
        List<Long> menuIds = roleDTO.getMenuIds();
        RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
        roleMenuDTO.setRoleId(role.getId());
        roleMenuDTO.setMenuIds(menuIds);
        bindMenu(roleMenuDTO);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Role role = new Role();
        role.setId(id);
        role.setStatus(status);
        roleRepository.updateById(role);
    }

    @Override
    public RoleDTO getById(Serializable id) {
        Role role = roleRepository.getById(id);
        return RoleAssembler.INSTANCE.entityToDto(role);
    }

    @Override
    public PageResult<RoleDTO> page(RoleQuery query) {
        // @formatter:off
        query.checkPage();
        Page<Role> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        roleRepository.list(query);
        List<RoleDTO> roleDTOs = page.getResult()
            .stream()
            .map(RoleAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), roleDTOs);
        // @formatter:on
    }

    @Override
    public List<RoleDTO> list(RoleQuery query) {
        // @formatter:off
        List<Role> list = roleRepository.list(query);
        return list
            .stream()
            .map(RoleAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        // @formatter:on
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindMenu(RoleMenuDTO roleMenuDTO) {
        // @formatter:off
        ValidUtil.valid(roleMenuDTO);
        Long roleId = roleMenuDTO.getRoleId();
        List<Long> menuIds = Optional.ofNullable(roleMenuDTO.getMenuIds()).orElseGet(ArrayList::new);
        List<RoleMenu> roleMenus = roleMenuRepository.listByRoleIds(Collections.singletonList(roleId));
        List<Long> selectMenuIds = roleMenus.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        // 需要添加角色用户关联关系
        List<RoleMenu> addRoleMenus = menuIds
            .stream()
            .filter(x-> !selectMenuIds.contains(x))
            .map(x-> convertToRoleMenu(roleId, x))
            .collect(Collectors.toList());
        // 需要删除的菜单id列表
        List<Long> removeMenuIds = selectMenuIds
            .stream()
            .filter(x-> !menuIds.contains(x))
            .collect(Collectors.toList());
        // 删除角色绑定的菜单
        roleMenuRepository.removeByRoleIdAndMenuIds(roleId, removeMenuIds);
        // 保存角色绑定的菜单
        roleMenuRepository.saveBatch(addRoleMenus);
        // @formatter:on
    }

    @Override
    public RoleMenuDtlDTO listByCodes(RoleDtlQuery roleDtlQuery) {
        return mergeRoleByCodes(roleDtlQuery.getRoleCodes(), roleDtlQuery.getShowAll(), roleDtlQuery.getTreeHandle());
    }

    @Override
    public RoleMenuDtlDTO listByIds(RoleDtlQuery roleDtlQuery) {
        return mergeRoleByIds(roleDtlQuery.getRoleIds(), roleDtlQuery.getShowAll(), roleDtlQuery.getTreeHandle());
    }

    private RoleMenu convertToRoleMenu(Long roleId, Long x) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        roleMenu.setMenuId(x);
        return roleMenu;
    }

}
