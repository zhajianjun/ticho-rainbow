package top.ticho.rainbow.domain.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.system.service.RoleService;
import top.ticho.rainbow.domain.handle.DictHandle;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.core.enums.CommonStatus;
import top.ticho.rainbow.infrastructure.entity.Role;
import top.ticho.rainbow.interfaces.assembler.RoleAssembler;
import top.ticho.rainbow.interfaces.dto.RoleDTO;
import top.ticho.rainbow.interfaces.dto.RoleMenuDTO;
import top.ticho.rainbow.interfaces.dto.RoleMenuDtlDTO;
import top.ticho.rainbow.interfaces.excel.RoleExp;
import top.ticho.rainbow.interfaces.query.RoleDtlQuery;
import top.ticho.rainbow.interfaces.query.RoleQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.valid.TiValidUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
public class RoleServiceImpl extends AbstractAuthServiceImpl implements RoleService {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private RoleMenuRepository roleMenuRepository;

    @Resource
    private UserRoleRepository userRoleRepository;

    @Resource
    private DictHandle dictHandle;

    @Resource
    private HttpServletResponse response;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleDTO roleDTO) {
        Role role = RoleAssembler.INSTANCE.dtoToEntity(roleDTO);
        Role dbDictType = roleRepository.getByCodeExcludeId(role.getCode(), null);
        TiAssert.isNull(dbDictType, TiBizErrCode.FAIL, "保存失败，角色已存在");
        TiAssert.isTrue(roleRepository.save(role), TiBizErrCode.FAIL, "保存失败");
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
        TiAssert.isTrue(!userRoleExists, TiBizErrCode.FAIL, "删除失败,请解绑所有的用户角色");
        boolean roleMenuExists = roleMenuRepository.existsByRoleIds(roleIds);
        TiAssert.isTrue(!roleMenuExists, TiBizErrCode.FAIL, "删除失败,请解绑所有的角色菜单");
        Role dbRole = roleRepository.getById(id);
        TiAssert.isNotNull(dbRole, TiBizErrCode.FAIL, "删除失败，角色不存在");
        TiAssert.isNotNull(Objects.equals(SecurityConst.ADMIN, dbRole.getCode()), TiBizErrCode.FAIL, "管理员角色不可删除");
        TiAssert.isTrue(roleRepository.removeById(id), TiBizErrCode.FAIL, "删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(RoleDTO roleDTO) {
        Role role = RoleAssembler.INSTANCE.dtoToEntity(roleDTO);
        Role repeatRole = roleRepository.getByCodeExcludeId(role.getCode(), role.getId());
        TiAssert.isNull(repeatRole, TiBizErrCode.FAIL, "修改失败，角色已存在");
        Role dbRole = roleRepository.getById(role.getId());
        // 角色编码不能修改
        role.setCode(dbRole.getCode());
        // 管理员角色一定是正常状态
        if (Objects.equals(SecurityConst.ADMIN, dbRole.getCode())) {
            role.setStatus(CommonStatus.NORMAL.code());
        }
        TiAssert.isTrue(roleRepository.updateById(role), TiBizErrCode.FAIL, "修改失败");
        List<Long> menuIds = roleDTO.getMenuIds();
        RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
        roleMenuDTO.setRoleId(role.getId());
        roleMenuDTO.setMenuIds(menuIds);
        bindMenu(roleMenuDTO);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Role dbRole = roleRepository.getById(id);
        TiAssert.isNotNull(dbRole, TiBizErrCode.FAIL, "修改失败，角色不存在");
        Role role = new Role();
        role.setId(id);
        // 管理员角色一定是正常状态
        if (Objects.equals(SecurityConst.ADMIN, dbRole.getCode())) {
            role.setStatus(CommonStatus.NORMAL.code());
        } else {
            role.setStatus(status);
        }
        roleRepository.updateById(role);
    }

    @Override
    public RoleDTO getById(Serializable id) {
        Role role = roleRepository.getById(id);
        return RoleAssembler.INSTANCE.entityToDto(role);
    }

    @Override
    public TiPageResult<RoleDTO> page(RoleQuery query) {
        query.checkPage();
        Page<Role> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        roleRepository.list(query);
        List<RoleDTO> roleDTOs = page.getResult()
            .stream()
            .map(RoleAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), roleDTOs);
    }

    @Override
    public List<RoleDTO> list(RoleQuery query) {
        List<Role> list = roleRepository.list(query);
        return list
            .stream()
            .map(RoleAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindMenu(RoleMenuDTO roleMenuDTO) {
        TiValidUtil.valid(roleMenuDTO);
        // 删除角色绑定的菜单
        roleMenuRepository.removeAndSave(roleMenuDTO.getRoleId(), roleMenuDTO.getMenuIds());
    }

    @Override
    public RoleMenuDtlDTO listRoleMenu(RoleDtlQuery roleDtlQuery) {
        return mergeRoleByIds(roleDtlQuery.getRoleIds(), roleDtlQuery.getShowAll(), roleDtlQuery.getTreeHandle());
    }

    @Override
    public void expExcel(RoleQuery query) throws IOException {
        String sheetName = "角色信息";
        String fileName = "角色信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<Integer, String> labelMap = dictHandle.getLabelMap(DictConst.COMMON_STATUS, NumberUtil::parseInt);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, RoleExp.class, response);
    }

    private Collection<RoleExp> excelExpHandle(RoleQuery query, Map<Integer, String> labelMap) {
        query.checkPage();
        Page<Role> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        roleRepository.list(query);
        return page.getResult()
            .stream()
            .map(x -> {
                RoleExp roleExp = RoleAssembler.INSTANCE.entityToExp(x);
                roleExp.setStatusName(labelMap.get(x.getStatus()));
                return roleExp;
            })
            .collect(Collectors.toList());
    }

}
