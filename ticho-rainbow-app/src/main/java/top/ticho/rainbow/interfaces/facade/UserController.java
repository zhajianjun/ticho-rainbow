package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.dto.command.UseModifyCommand;
import top.ticho.rainbow.application.dto.command.UseSaveCommand;
import top.ticho.rainbow.application.dto.command.UserModifyPasswordCommand;
import top.ticho.rainbow.application.dto.command.VersionModifyCommand;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.application.dto.response.UserDTO;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

/**
 * 用户信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    /**
     * 保存用户
     */
    @ApiLog("保存用户")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody UseSaveCommand useSaveCommand) {
        userService.save(useSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除用户
     */
    @ApiLog("删除用户")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        userService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改用户
     */
    @ApiLog("修改用户")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody UseModifyCommand useModifyCommand) {
        userService.modify(useModifyCommand);
        return TiResult.ok();
    }

    /**
     * 锁定用户
     */
    @ApiLog("锁定用户")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_LOCK + "')")
    @PatchMapping("status/lock")
    public TiResult<Void> lock(
        @NotNull(message = "用户信息不能为空")
        @NotNull(message = "用户信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        userService.lock(datas);
        return TiResult.ok();
    }

    /**
     * 解锁用户
     */
    @ApiLog("解锁用户")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_UNLOCK + "')")
    @PatchMapping("status/un-lock")
    public TiResult<Void> unLock(
        @NotNull(message = "用户信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        userService.unLock(datas);
        return TiResult.ok();
    }


    /**
     * 注销用户
     */
    @ApiLog("注销用户")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_LOG_OUT + "')")
    @PatchMapping("status/log-out")
    public TiResult<Void> logOut(
        @NotNull(message = "用户信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        userService.logOut(datas);
        return TiResult.ok();
    }

    /**
     * 修改用户密码
     */
    @ApiLog("修改用户密码")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_PASSWORD_MODIFY + "')")
    @PatchMapping("password")
    public TiResult<Void> modifyPassword(@Validated @RequestBody UserModifyPasswordCommand userModifyPasswordCommand) {
        userService.modifyPassword(userModifyPasswordCommand);
        return TiResult.ok();
    }


    /**
     * 重置用户密码
     */
    @ApiLog("重置用户密码")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_PASSWORD_RESET + "')")
    @PatchMapping("password/reset")
    public TiResult<Void> resetPassword(
        @NotNull(message = "用户信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        userService.resetPassword(datas);
        return TiResult.ok();
    }

    /**
     * 查询用户(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<UserDTO>> page(@Validated UserQuery query) {
        return TiResult.ok(userService.page(query));
    }

    /**
     * 下载用户导入模板
     */
    @ApiLog("下载用户导入模板")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_IMPORT_TEMPLATE_DOWNLOAD + "')")
    @GetMapping("excel-template/download")
    public void excelTemplateDownload() throws IOException {
        userService.excelTemplateDownload();
    }

    /**
     * 导入用户
     */
    @ApiLog("导入用户")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_IMPORT + "')")
    @PostMapping("excel/import")
    public void importExcel(@RequestPart("file") MultipartFile file) throws IOException {
        userService.importExcel(file);
    }

    /**
     * 导出用户
     */
    @ApiLog("导出用户")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_USER_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated UserQuery query) throws IOException {
        userService.exportExcel(query);
    }

}
