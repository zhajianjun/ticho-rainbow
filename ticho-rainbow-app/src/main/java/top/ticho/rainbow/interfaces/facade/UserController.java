package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.dto.PasswordDTO;
import top.ticho.rainbow.application.dto.UserDTO;
import top.ticho.rainbow.application.dto.UserPasswordDTO;
import top.ticho.rainbow.application.dto.UserRoleDTO;
import top.ticho.rainbow.application.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

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
    public TiResult<Void> save(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return TiResult.ok();
    }

    /**
     * 锁定用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     */
    @PreAuthorize("@perm.hasPerms('system:user:lock')")
    @PostMapping("lock")
    public TiResult<Void> lock(@RequestBody List<String> usernames) {
        userService.lock(usernames);
        return TiResult.ok();
    }

    /**
     * 解锁用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     */
    @PreAuthorize("@perm.hasPerms('system:user:unLock')")
    @PostMapping("unLock")
    public TiResult<Void> unLock(@RequestBody List<String> usernames) {
        userService.unLock(usernames);
        return TiResult.ok();
    }


    /**
     * 注销用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     */
    @PreAuthorize("@perm.hasPerms('system:user:logOut')")
    @PostMapping("logOut")
    public TiResult<Void> logOut(@RequestBody List<String> usernames) {
        userService.logOut(usernames);
        return TiResult.ok();
    }

    /**
     * 删除用户
     *
     * @param usernames 用户名, 多个用逗号隔开
     * @return {@link TiResult }<{@link Void }>
     */
    @PreAuthorize("@perm.hasPerms('system:user:remove')")
    @PostMapping("remove")
    public TiResult<Void> remove(@RequestBody List<String> usernames) {
        userService.remove(usernames);
        return TiResult.ok();
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@perm.hasPerms('system:user:modify')")
    @PutMapping
    public TiResult<Void> modify(@RequestBody UserDTO userDTO) {
        userService.modify(userDTO);
        return TiResult.ok();
    }

    /**
     * 修改用户(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:user:updateForSelf')")
    @PutMapping("updateForSelf")
    public TiResult<Void> updateForSelf(@RequestBody UserDTO userDTO) {
        userService.updateForSelf(userDTO);
        return TiResult.ok();
    }

    /**
     * 上传用户头像
     *
     * @param file 文件
     */
    @PreAuthorize("@perm.hasPerms('system:user:uploadAvatar')")
    @PostMapping("uploadAvatar")
    public TiResult<String> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return TiResult.ok(userService.uploadAvatar(file));
    }

    /**
     * 重置用户密码
     *
     * @param username 用户名
     */
    @PreAuthorize("@perm.hasPerms('system:user:resetPassword')")
    @PutMapping("resetPassword")
    public TiResult<Void> resetPassword(String username) {
        userService.resetPassword(username);
        return TiResult.ok();
    }

    /**
     * 修改用户密码
     */
    @PreAuthorize("@perm.hasPerms('system:user:updatePassword')")
    @PutMapping("updatePassword")
    public TiResult<Void> updatePassword(@RequestBody UserPasswordDTO userDetailDto) {
        userService.updatePassword(userDetailDto);
        return TiResult.ok();
    }

    /**
     * 修改用户密码(登录人)
     */
    @PreAuthorize("@perm.hasPerms('system:user:updatePasswordForSelf')")
    @PutMapping("updatePasswordForSelf")
    public TiResult<Void> updatePasswordForSelf(@RequestBody PasswordDTO passwordDTO) {
        userService.updatePasswordForSelf(passwordDTO);
        return TiResult.ok();
    }

    /**
     * 查询用户
     *
     * @param username 用户名
     * @return {@link TiResult }<{@link UserDTO }>
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
    @GetMapping("infoForSelf")
    public TiResult<UserDTO> info() {
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
    @GetMapping("detailForSelf")
    public TiResult<UserRoleMenuDtlDTO> detailForSelf() {
        return TiResult.ok(userService.getUserDtl());
    }

    /**
     * 查询全部用户(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:user:page')")
    @GetMapping
    public TiResult<TiPageResult<UserDTO>> page(@RequestBody UserQuery query) {
        return TiResult.ok(userService.page(query));
    }

    /**
     * 绑定用户角色
     */
    @PreAuthorize("@perm.hasPerms('system:user:bindRole')")
    @PostMapping("bindRole")
    public TiResult<Void> bindRole(@RequestBody UserRoleDTO userRoleDTO) {
        userService.bindRole(userRoleDTO);
        return TiResult.ok();
    }

    /**
     * 下载导入模板
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impTemplate')")
    @PostMapping("impTemplate")
    public void impTemplate() throws IOException {
        userService.impTemplate();
    }

    /**
     * 导入用户
     *
     * @param file 文件
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impExcel')")
    @PostMapping("impExcel")
    public void impExcel(@RequestPart("file") MultipartFile file) throws IOException {
        userService.impExcel(file);
    }

    /**
     * 导出用户
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:expExcel')")
    @PostMapping("expExcel")
    public void expExcel(@RequestBody UserQuery query) throws IOException {
        userService.expExcel(query);
    }

}
