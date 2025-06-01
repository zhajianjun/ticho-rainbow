package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.assembler.UserAssembler;
import top.ticho.rainbow.application.dto.command.UseModifyCommand;
import top.ticho.rainbow.application.dto.command.UseSaveCommand;
import top.ticho.rainbow.application.dto.command.UseVersionModifyCommand;
import top.ticho.rainbow.application.dto.command.UserModifyPasswordCommand;
import top.ticho.rainbow.application.dto.excel.UserExcelExport;
import top.ticho.rainbow.application.dto.excel.UserExcelImport;
import top.ticho.rainbow.application.dto.excel.UserExcelImportModel;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.application.dto.response.UserDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.executor.UserExecutor;
import top.ticho.rainbow.application.repository.UserAppRepository;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.entity.UserRole;
import top.ticho.rainbow.domain.entity.vo.UserModifyVO;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.enums.UserStatus;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiSpringUtil;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 用户信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAppRepository userAppRepository;
    private final UserAssembler userAssembler;
    private final UserRoleRepository userRoleRepository;
    private final HttpServletResponse response;
    private final RoleRepository roleRepository;
    private final DictExecutor dictExecutor;
    private final UserExecutor userExecutor;

    @Transactional(rollbackFor = Exception.class)
    public void save(UseSaveCommand useSaveCommand) {
        String password = useSaveCommand.getPassword();
        useSaveCommand.setPassword(userExecutor.encodePassword(password));
        User user = userAssembler.toEntity(useSaveCommand);
        userExecutor.checkRepeat(user);
        TiAssert.isTrue(userRepository.save(user), "保存失败");
        if (CollUtil.isEmpty(useSaveCommand.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), useSaveCommand.getRoleIds());
    }

    public void remove(Long id, Long version) {
        User user = userRepository.find(id);
        TiAssert.isNotNull(user, "删除失败, 用户不存在");
        user.checkVersion(version, "数据已被修改，请刷新后重试");
        TiAssert.isTrue(Objects.equals(UserStatus.LOG_OUT.code(), user.getStatus()), "");
        TiAssert.isTrue(userRepository.remove(user), "修改失败，请刷新后重试");
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(UseModifyCommand useModifyCommand) {
        User user = userRepository.find(useModifyCommand.getId());
        TiAssert.isNotNull(user, "修改失败, 用户不存在");
        user.checkVersion(useModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        UserModifyVO modifyVo = userAssembler.toModifyVo(useModifyCommand);
        List<String> errors = userExecutor.checkRepeat(user);
        TiAssert.isEmpty(errors, () -> errors.get(0));
        user.modify(modifyVo);
        TiAssert.isTrue(userRepository.modify(user), "修改失败，请刷新后重试");
        userRoleRepository.removeAndSave(useModifyCommand.getId(), useModifyCommand.getRoleIds());
    }

    public void lock(List<UseVersionModifyCommand> modifys) {
        boolean lock = modifyBatch(modifys, User::lock);
        TiAssert.isTrue(lock, "锁定失败，请刷新后重试");
    }

    public void unLock(List<UseVersionModifyCommand> modifys) {
        boolean unLock = modifyBatch(modifys, User::unLock);
        TiAssert.isTrue(unLock, "解锁失败，请刷新后重试");
    }

    public void logOut(List<UseVersionModifyCommand> modifys) {
        boolean logOut = modifyBatch(modifys, User::logOut);
        TiAssert.isTrue(logOut, "注销失败，请刷新后重试");
    }

    public void modifyPassword(UserModifyPasswordCommand userModifyPasswordCommand) {
        User user = userRepository.find(userModifyPasswordCommand.getId());
        TiAssert.isNotEmpty(user, "用户不存在");
        user.checkVersion(userModifyPasswordCommand.getVersion(), "数据已被修改，请刷新后重试");
        userExecutor.modifyPassword(userModifyPasswordCommand.getPassword(), userModifyPasswordCommand.getNewPassword(), user);
    }

    public void resetPassword(List<UseVersionModifyCommand> modifys) {
        boolean modifyPassword = modifyBatch(modifys, user -> {
            String encodedPasswordNew = userExecutor.encodePassword(CommConst.DEFAULT_PASSWORD);
            user.modifyPassword(encodedPasswordNew);
        });
        TiAssert.isTrue(modifyPassword, "重置密码失败，请刷新后重试");
    }

    public TiPageResult<UserDTO> page(UserQuery query) {
        TiPageResult<UserDTO> page = userAppRepository.page(query);
        userExecutor.setRoles(page.getRows());
        return page;
    }

    private boolean modifyBatch(List<UseVersionModifyCommand> modifys, Consumer<User> modifyHandle) {
        List<Long> ids = CollStreamUtil.toList(modifys, UseVersionModifyCommand::getId);
        List<User> users = userRepository.list(ids);
        Map<Long, User> userMap = CollStreamUtil.toIdentityMap(users, User::getId);
        for (UseVersionModifyCommand modify : modifys) {
            User user = userMap.get(modify.getId());
            TiAssert.isNotNull(user, StrUtil.format("操作失败, 用户不存在, id: {}", modify.getId()));
            user.checkVersion(modify.getVersion(), StrUtil.format("数据已被修改，请刷新后重试, 用户: {}", user.getUsername()));
            // 修改逻辑
            modifyHandle.accept(user);
        }
        return userRepository.modifyBatch(users);
    }

    public void excelTemplateDownload() throws IOException {
        String sheetName = "用户信息";
        String fileName = "用户信息模板-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        ExcelHandle.writeEmptyToResponseBatch(fileName, sheetName, UserExcelImportModel.class, response);
    }

    public void importExcel(MultipartFile file) throws IOException {
        String sheetName = "导入结果";
        String fileName = StrUtil.format("{}-导入结果", file.getOriginalFilename());
        Role guestRole = roleRepository.getGuestRole();
        TiAssert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        UserService bean = TiSpringUtil.getBean(this.getClass());
        Map<String, Integer> valueMap = dictExecutor.getValueMap(DictConst.SEX, NumberUtil::parseInt);
        ExcelHandle.readAndWriteToResponse((x, y) -> bean.readAndWrite(x, y, guestRole, valueMap), file, fileName, sheetName, UserExcelImport.class, response);
    }

    @Transactional(rollbackFor = Exception.class)
    public void readAndWrite(List<UserExcelImport> userExcelImports, Consumer<UserExcelImport> errHandle, Role guestRole, Map<String, Integer> valueMap) {
        List<User> users = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();
        for (UserExcelImport userExcelImport : userExcelImports) {
            if (userExcelImport.getIsError()) {
                continue;
            }
            String password = userExecutor.encodePassword(CommConst.DEFAULT_PASSWORD);
            User user = userAssembler.toEntity(userExcelImport, password, valueMap.get(userExcelImport.getSexName()));
            List<String> errorMsgs = userExecutor.checkRepeat(user);
            if (CollUtil.isNotEmpty(errorMsgs)) {
                userExcelImport.setMessage(String.join(",", errorMsgs));
                errHandle.accept(userExcelImport);
                continue;
            }
            userExcelImport.setMessage("导入成功");
            users.add(user);
            UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(guestRole.getId())
                .build();
            userRoles.add(userRole);
        }
        if (CollUtil.isEmpty(users)) {
            return;
        }
        userRepository.saveBatch(users);
        userRoleRepository.saveBatch(userRoles);
    }


    public void exportExcel(UserQuery query) throws IOException {
        String sheetName = "用户信息";
        String fileName = "用户信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.USER_STATUS, DictConst.SEX);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, UserExcelExport.class, response);
    }

    private Collection<UserExcelExport> excelExpHandle(UserQuery query, Map<String, String> labelMap) {
        TiPageResult<UserDTO> page = userAppRepository.page(query);
        return page.getRows()
            .stream()
            .map(x -> {
                UserExcelExport userExcelExport = userAssembler.toExcelExport(x);
                userExcelExport.setStatusName(labelMap.get(DictConst.USER_STATUS + x.getStatus()));
                userExcelExport.setSexName(labelMap.get(DictConst.SEX + x.getSex()));
                return userExcelExport;
            })
            .collect(Collectors.toList());
    }

}
