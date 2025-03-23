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
import top.ticho.rainbow.application.dto.command.UserModifySelfPasswordCommand;
import top.ticho.rainbow.application.dto.response.UserDTO;
import top.ticho.rainbow.application.dto.command.UserModifyPasswordCommand;
import top.ticho.rainbow.application.dto.command.UserBindRoleCommand;
import top.ticho.rainbow.application.dto.response.UserRoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.command.UseModifyCommand;
import top.ticho.rainbow.application.dto.command.UseModifySelfCommand;
import top.ticho.rainbow.application.dto.command.UseSaveCommand;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 用户信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    /**
     * 保存用户
     */
    @PreAuthorize("@perm.hasPerms('system:user:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody UseSaveCommand useSaveCommand) {
        userService.save(useSaveCommand);
        return TiResult.ok();
    }

    /**
     * 上传用户头像
     *
     * @param file 文件
     */
    @PreAuthorize("@perm.hasPerms('system:user:uploadAvatar')")
    @PostMapping("avatar")
    public TiResult<String> uploadAvatar(@NotNull(message = "请上传头像") MultipartFile file) {
        return TiResult.ok(userService.uploadAvatar(file));
    }

    /**
     * 删除用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     * @return {@link TiResult }<{@link Void }>
     */
    @PreAuthorize("@perm.hasPerms('system:user:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody List<String> usernames) {
        userService.remove(usernames);
        return TiResult.ok();
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@perm.hasPerms('system:user:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody UseModifyCommand useModifyCommand) {
        userService.modify(useModifyCommand);
        return TiResult.ok();
    }

    /**
     * 修改用户(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:user:modifyForSelf')")
    @PutMapping("self")
    public TiResult<Void> modifySelf(@Validated @RequestBody UseModifySelfCommand useModifySelfCommand) {
        userService.modifyForSelf(useModifySelfCommand);
        return TiResult.ok();
    }

    /**
     * 锁定用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     */
    @PreAuthorize("@perm.hasPerms('system:user:lock')")
    @PatchMapping("status/lock")
    public TiResult<Void> lock(@NotNull(message = "用户名不能为空") @RequestBody List<String> usernames) {
        userService.lock(usernames);
        return TiResult.ok();
    }

    /**
     * 解锁用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     */
    @PreAuthorize("@perm.hasPerms('system:user:unLock')")
    @PatchMapping("status/un-lock")
    public TiResult<Void> unLock(@NotNull(message = "用户名不能为空") @RequestBody List<String> usernames) {
        userService.unLock(usernames);
        return TiResult.ok();
    }


    /**
     * 注销用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     */
    @PreAuthorize("@perm.hasPerms('system:user:logOut')")
    @PatchMapping("status/log-out")
    public TiResult<Void> logOut(@NotNull(message = "用户名不能为空") @RequestBody List<String> usernames) {
        userService.logOut(usernames);
        return TiResult.ok();
    }

    /**
     * 修改用户密码
     */
    @PreAuthorize("@perm.hasPerms('system:user:updatePassword')")
    @PatchMapping("password")
    public TiResult<Void> modifyPassword(@Validated @RequestBody UserModifyPasswordCommand userModifyPasswordCommand) {
        userService.modifyPassword(userModifyPasswordCommand);
        return TiResult.ok();
    }

    /**
     * 修改用户密码(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:user:updatePasswordForSelf')")
    @PatchMapping("self-password")
    public TiResult<Void> modifySelfPassword(@Validated @RequestBody UserModifySelfPasswordCommand userModifySelfPasswordCommand) {
        userService.modifyPasswordForSelf(userModifySelfPasswordCommand);
        return TiResult.ok();
    }

    /**
     * 重置用户密码
     *
     * @param username 用户名
     */
    @PreAuthorize("@perm.hasPerms('system:user:resetPassword')")
    @PatchMapping("password/reset")
    public TiResult<Void> resetPassword(String username) {
        userService.resetPassword(username);
        return TiResult.ok();
    }


    /**
     * 查询用户
     *
     * @param username 用户名
     */
    @PreAuthorize("@perm.hasPerms('system:user:info')")
    @GetMapping("info")
    public TiResult<UserDTO> info(String username) {
        return TiResult.ok(userService.getInfoByUsername(username));
    }

    /**
     * 查询用户(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:user:infoForSelf')")
    @GetMapping("self-info")
    public TiResult<UserDTO> selfInfo() {
        return TiResult.ok(userService.getInfo());
    }

    /**
     * 查询用户角色菜单权限
     *
     * @param username 用户名
     */
    @PreAuthorize("@perm.hasPerms('system:user:detail')")
    @GetMapping("detail")
    public TiResult<UserRoleMenuDtlDTO> detail(String username) {
        return TiResult.ok(userService.getUserDtl(username));
    }

    /**
     * 查询用户角色菜单权限(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:user:detailForSelf')")
    @GetMapping("self-detail")
    public TiResult<UserRoleMenuDtlDTO> selfDetail() {
        return TiResult.ok(userService.getUserDtl());
    }

    /**
     * 查询用户(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:user:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<UserDTO>> page(@Validated UserQuery query) {
        return TiResult.ok(userService.page(query));
    }

    /**
     * 绑定用户角色
     */
    @PreAuthorize("@perm.hasPerms('system:user:bindRole')")
    @PostMapping("role/bind")
    public TiResult<Void> bindRole(@Validated @RequestBody UserBindRoleCommand userBindRoleCommand) {
        userService.bindRole(userBindRoleCommand);
        return TiResult.ok();
    }

    /**
     * 下载导入模板
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impTemplate')")
    @GetMapping("excel-template/download")
    public void excelTemplateDownload() throws IOException {
        userService.excelTemplateDownload();
    }

    /**
     * 导入用户
     *
     * @param file 文件
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:importExcel')")
    @PostMapping("excel/import")
    public void importExcel(@RequestPart("file") MultipartFile file) throws IOException {
        userService.importExcel(file);
    }

    /**
     * 导出用户
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:exportExcel')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated @RequestBody UserQuery query) throws IOException {
        userService.exportExcel(query);
    }

}
