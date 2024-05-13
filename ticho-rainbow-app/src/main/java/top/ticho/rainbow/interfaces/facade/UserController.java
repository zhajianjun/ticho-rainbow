package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.rainbow.interfaces.dto.PasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.dto.UserPasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleMenuDtlDTO;
import top.ticho.rainbow.interfaces.query.UserQuery;

import java.io.IOException;
import java.util.List;

/**
 * 用户信息 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户信息")
@ApiSort(30)
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("@perm.hasPerms('system:user:save')")
    @ApiOperation(value = "保存用户")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:lock')")
    @ApiOperation(value = "锁定用户")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("lock")
    public Result<Void> lock(@RequestBody List<String> username) {
        userService.lock(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:unLock')")
    @ApiOperation(value = "解锁用户")
    @ApiOperationSupport(order = 30)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("unLock")
    public Result<Void> unLock(@RequestBody List<String> username) {
        userService.unLock(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:logOut')")
    @ApiOperation(value = "注销用户")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("logOut")
    public Result<Void> logOut(@RequestBody List<String> username) {
        userService.logOut(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:remove')")
    @ApiOperation(value = "删除用户")
    @ApiOperationSupport(order = 50)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("remove")
    public Result<Void> remove(@RequestBody List<String> username) {
        userService.remove(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:update')")
    @ApiOperation(value = "修改用户")
    @ApiOperationSupport(order = 60)
    @PutMapping
    public Result<Void> update(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:updateForSelf')")
    @ApiOperation(value = "修改用户(登录人)")
    @ApiOperationSupport(order = 70)
    @PutMapping("updateForSelf")
    public Result<Void> updateForSelf(@RequestBody UserDTO userDTO) {
        userService.updateForSelf(userDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:uploadAvatar')")
    @ApiOperation(value = "上传用户头像")
    @ApiOperationSupport(order = 80)
    @PostMapping("uploadAvatar")
    public Result<String> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return Result.ok(userService.uploadAvatar(file));
    }

    @PreAuthorize("@perm.hasPerms('system:user:resetPassword')")
    @ApiOperation(value = "重置用户密码")
    @ApiOperationSupport(order = 90)
    @PutMapping("resetPassword")
    public Result<Void> resetPassword(String username) {
        userService.resetPassword(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:updatePassword')")
    @ApiOperation(value = "修改用户密码")
    @ApiOperationSupport(order = 100)
    @PutMapping("updatePassword")
    public Result<Void> updatePassword(@RequestBody UserPasswordDTO userDetailDto) {
        userService.updatePassword(userDetailDto);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:updatePasswordForSelf')")
    @ApiOperation(value = "修改用户密码(登录人)")
    @ApiOperationSupport(order = 110)
    @PutMapping("updatePasswordForSelf")
    public Result<Void> updatePasswordForSelf(@RequestBody PasswordDTO passwordDTO) {
        userService.updatePasswordForSelf(passwordDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:info')")
    @ApiOperation(value = "查询用户")
    @ApiOperationSupport(order = 120)
    @ApiImplicitParam(value = "用户名", name = "username", required = true)
    @GetMapping("info")
    public Result<UserDTO> info(String username) {
        return Result.ok(userService.getInfoByUsername(username));
    }

    @PreAuthorize("@perm.hasPerms('system:user:infoForSelf')")
    @ApiOperation(value = "查询用户(登录人)")
    @ApiOperationSupport(order = 130)
    @GetMapping("infoForSelf")
    public Result<UserDTO> info() {
        return Result.ok(userService.getInfo());
    }

    @PreAuthorize("@perm.hasPerms('system:user:detail')")
    @ApiOperation(value = "查询用户角色菜单权限")
    @ApiOperationSupport(order = 140)
    @GetMapping("detail")
    public Result<UserRoleMenuDtlDTO> detail(String username) {
        return Result.ok(userService.getUserDtl(username));
    }

    @PreAuthorize("@perm.hasPerms('system:user:detailForSelf')")
    @ApiOperation(value = "查询用户角色菜单权限(登录人)")
    @ApiOperationSupport(order = 150)
    @GetMapping("detailForSelf")
    public Result<UserRoleMenuDtlDTO> detailForSelf() {
        return Result.ok(userService.getUserDtl());
    }

    @PreAuthorize("@perm.hasPerms('system:user:page')")
    @ApiOperation(value = "查询全部用户(分页)")
    @ApiOperationSupport(order = 160)
    @PostMapping("page")
    public Result<PageResult<UserDTO>> page(@RequestBody UserQuery query) {
        return Result.ok(userService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:user:bindRole')")
    @ApiOperation(value = "绑定用户角色")
    @ApiOperationSupport(order = 170)
    @PostMapping("bindRole")
    public Result<Void> bindRole(@RequestBody UserRoleDTO userRoleDTO) {
        userService.bindRole(userRoleDTO);
        return Result.ok();
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impTemplate')")
    @ApiOperation(value = "下载导入模板", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 180)
    @PostMapping("impTemplate")
    public void impTemplate() throws IOException {
        userService.impTemplate();
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impExcel')")
    @ApiOperation(value = "导入用户", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 190)
    @PostMapping("impExcel")
    public void impExcel(@RequestPart("file") MultipartFile file) throws IOException {
        userService.impExcel(file);
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:expExcel')")
    @ApiOperation(value = "导出用户", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 200)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody UserQuery query) throws IOException {
        userService.expExcel(query);
    }

}
