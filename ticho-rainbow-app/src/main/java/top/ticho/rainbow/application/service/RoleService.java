package top.ticho.rainbow.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.RoleAssembler;
import top.ticho.rainbow.application.dto.excel.RoleExcelExport;
import top.ticho.rainbow.application.executor.AuthExecutor;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.RoleAppRepository;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.vo.RoleModifyVO;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DateConst;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.rainbow.interfaces.command.RoleBindMenuCommand;
import top.ticho.rainbow.interfaces.command.RoleModifyCommand;
import top.ticho.rainbow.interfaces.command.RoleSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.RoleDTO;
import top.ticho.rainbow.interfaces.dto.RoleMenuDTO;
import top.ticho.rainbow.interfaces.query.RoleDtlQuery;
import top.ticho.rainbow.interfaces.query.RoleQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.tool.core.TiAssert;
import top.ticho.tool.core.TiNumberUtil;
import top.ticho.tool.core.TiStrUtil;
import top.ticho.tool.valid.TiValidUtil;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleAppRepository roleAppRepository;
    private final RoleAssembler roleAssembler;
    private final RoleMenuRepository roleMenuRepository;
    private final UserRoleRepository userRoleRepository;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;
    private final AuthExecutor authExecutor;

    @Transactional(rollbackFor = Exception.class)
    public void save(RoleSaveCommand roleSaveCommand) {
        Role role = roleAssembler.toEntity(roleSaveCommand);
        Role dbDictType = roleRepository.getByCodeExcludeId(role.getCode(), null);
        TiAssert.isNull(dbDictType, "保存失败，角色已存在");
        TiAssert.isTrue(roleRepository.save(role), "保存失败");
        List<Long> menuIds = roleSaveCommand.getMenuIds();
        RoleBindMenuCommand roleBindMenuCommand = new RoleBindMenuCommand();
        roleBindMenuCommand.setRoleId(role.getId());
        roleBindMenuCommand.setMenuIds(menuIds);
        bindMenu(roleBindMenuCommand);
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(VersionModifyCommand command) {
        List<Long> roleIds = Collections.singletonList(command.getId());
        boolean userRoleExists = userRoleRepository.existsByRoleIds(roleIds);
        TiAssert.isTrue(!userRoleExists, "删除失败,请解绑所有的用户角色");
        boolean roleMenuExists = roleMenuRepository.existsByRoleIds(roleIds);
        TiAssert.isTrue(!roleMenuExists, "删除失败,请解绑所有的角色菜单");
        Role role = roleRepository.find(command.getId());
        TiAssert.isNotNull(role, "删除失败，角色不存在");
        role.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(!role.isEnable(), "删除失败，请先禁用该角色");
        TiAssert.isNotNull(Objects.equals(SecurityConst.ADMIN, role.getCode()), "管理员角色不可删除");
        TiAssert.isTrue(roleRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(RoleModifyCommand roleModifyCommand) {
        Role role = roleRepository.find(roleModifyCommand.getId());
        TiAssert.isNotNull(role, "修改失败，角色不存在");
        role.checkVersion(roleModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        RoleModifyVO modifyVO = roleAssembler.toVo(roleModifyCommand);
        role.modify(modifyVO);
        TiAssert.isTrue(roleRepository.modify(role), "修改失败，请刷新后重试");
        List<Long> menuIds = roleModifyCommand.getMenuIds();
        RoleBindMenuCommand roleBindMenuCommand = new RoleBindMenuCommand();
        roleBindMenuCommand.setRoleId(role.getId());
        roleBindMenuCommand.setMenuIds(menuIds);
        bindMenu(roleBindMenuCommand);
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, Role::enable);
        TiAssert.isTrue(enable, "启用失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, Role::disable);
        TiAssert.isTrue(disable, "禁用失败，请刷新后重试");
    }

    public TiPageResult<RoleDTO> page(RoleQuery query) {
        return roleAppRepository.page(query);
    }

    public List<RoleDTO> all() {
        return roleAppRepository.all();
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindMenu(RoleBindMenuCommand bindMenuCommand) {
        TiValidUtil.valid(bindMenuCommand);
        // 删除角色绑定的菜单
        roleMenuRepository.removeAndSave(bindMenuCommand.getRoleId(), bindMenuCommand.getMenuIds());
    }

    public RoleMenuDTO listRoleMenu(RoleDtlQuery roleDtlQuery) {
        return authExecutor.mergeRoleByIds(roleDtlQuery.getRoleIds(), roleDtlQuery.getShowAll(), roleDtlQuery.getTreeHandle());
    }

    public void exportExcel(RoleQuery query) throws IOException {
        String sheetName = "角色信息";
        String fileName = "角色信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateConst.PURE_DATETIME_PATTERN));
        Map<Integer, String> labelMap = dictExecutor.getLabelMap(DictConst.COMMON_STATUS, TiNumberUtil::parseInt);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, RoleExcelExport.class, response);
    }

    private Collection<RoleExcelExport> excelExpHandle(RoleQuery query, Map<Integer, String> labelMap) {
        TiPageResult<RoleDTO> page = roleAppRepository.page(query);
        return page.getRows()
            .stream()
            .map(x -> {
                RoleExcelExport roleExcelExport = roleAssembler.toExcelExport(x);
                roleExcelExport.setStatusName(labelMap.get(x.getStatus()));
                return roleExcelExport;
            })
            .collect(Collectors.toList());
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<Role> modifyHandle) {
        List<Long> ids = modifys.stream().map(VersionModifyCommand::getId).collect(Collectors.toList());
        List<Role> roles = roleRepository.list(ids);
        Map<Long, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, Function.identity(), (o, n) -> o));
        for (VersionModifyCommand modify : modifys) {
            Role role = roleMap.get(modify.getId());
            TiAssert.isNotNull(role, TiStrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            role.checkVersion(modify.getVersion(), TiStrUtil.format("数据已被修改，请刷新后重试, 角色: {}", role.getName()));
            // 修改逻辑
            modifyHandle.accept(role);
        }
        return roleRepository.modifyBatch(roles);
    }

}
