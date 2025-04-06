package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.RoleAssembler;
import top.ticho.rainbow.application.dto.command.RoleBindMenuCommand;
import top.ticho.rainbow.application.dto.command.RoleModifyCommand;
import top.ticho.rainbow.application.dto.command.RoleSaveCommand;
import top.ticho.rainbow.application.dto.command.RoleStatusModifyCommand;
import top.ticho.rainbow.application.dto.excel.RoleExcelExport;
import top.ticho.rainbow.application.dto.query.RoleDtlQuery;
import top.ticho.rainbow.application.dto.query.RoleQuery;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.application.dto.response.RoleMenuDtlDTO;
import top.ticho.rainbow.application.executor.AuthExecutor;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.RoleAppRepository;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.vo.RoleModifyVO;
import top.ticho.rainbow.domain.repository.RoleMenuRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.valid.TiValidUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        TiAssert.isNull(dbDictType, TiBizErrCode.FAIL, "保存失败，角色已存在");
        TiAssert.isTrue(roleRepository.save(role), TiBizErrCode.FAIL, "保存失败");
        List<Long> menuIds = roleSaveCommand.getMenuIds();
        RoleBindMenuCommand roleBindMenuCommand = new RoleBindMenuCommand();
        roleBindMenuCommand.setRoleId(role.getId());
        roleBindMenuCommand.setMenuIds(menuIds);
        bindMenu(roleBindMenuCommand);
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        List<Long> roleIds = Collections.singletonList(id);
        boolean userRoleExists = userRoleRepository.existsByRoleIds(roleIds);
        TiAssert.isTrue(!userRoleExists, TiBizErrCode.FAIL, "删除失败,请解绑所有的用户角色");
        boolean roleMenuExists = roleMenuRepository.existsByRoleIds(roleIds);
        TiAssert.isTrue(!roleMenuExists, TiBizErrCode.FAIL, "删除失败,请解绑所有的角色菜单");
        Role dbRole = roleRepository.find(id);
        TiAssert.isNotNull(dbRole, TiBizErrCode.FAIL, "删除失败，角色不存在");
        TiAssert.isNotNull(Objects.equals(SecurityConst.ADMIN, dbRole.getCode()), TiBizErrCode.FAIL, "管理员角色不可删除");
        TiAssert.isTrue(roleRepository.remove(id), TiBizErrCode.FAIL, "删除失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(RoleModifyCommand roleModifyCommand) {
        Role role = roleRepository.find(roleModifyCommand.getId());
        TiAssert.isNotNull(role, TiBizErrCode.FAIL, "修改失败，角色已存在");
        RoleModifyVO modifyVO = roleAssembler.toVo(roleModifyCommand);
        role.modify(modifyVO);
        TiAssert.isTrue(roleRepository.modify(role), TiBizErrCode.FAIL, "修改失败");
        List<Long> menuIds = roleModifyCommand.getMenuIds();
        RoleBindMenuCommand roleBindMenuCommand = new RoleBindMenuCommand();
        roleBindMenuCommand.setRoleId(role.getId());
        roleBindMenuCommand.setMenuIds(menuIds);
        bindMenu(roleBindMenuCommand);
    }

    public void modifyStatus(RoleStatusModifyCommand roleStatusModifyCommand) {
        Role role = roleRepository.find(roleStatusModifyCommand.getId());
        TiAssert.isNotNull(role, TiBizErrCode.FAIL, "修改失败，角色不存在");
        role.modifyStatus(roleStatusModifyCommand.getStatus(), roleStatusModifyCommand.getVersion());
        roleRepository.modify(role);
    }

    public RoleDTO find(Long id) {
        Role role = roleRepository.find(id);
        return roleAssembler.toDTO(role);
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

    public RoleMenuDtlDTO listRoleMenu(RoleDtlQuery roleDtlQuery) {
        return authExecutor.mergeRoleByIds(roleDtlQuery.getRoleIds(), roleDtlQuery.getShowAll(), roleDtlQuery.getTreeHandle());
    }

    public void exportExcel(RoleQuery query) throws IOException {
        String sheetName = "角色信息";
        String fileName = "角色信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<Integer, String> labelMap = dictExecutor.getLabelMap(DictConst.COMMON_STATUS, NumberUtil::parseInt);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, RoleExcelExport.class, response);
    }

    private Collection<RoleExcelExport> excelExpHandle(RoleQuery query, Map<Integer, String> labelMap) {
        query.checkPage();
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

}
