package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.servlet.HandlerMapping;
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
    @Qualifier("viewControllerHandlerMapping")
    @Autowired
    private HandlerMapping viewControllerHandlerMapping;

    @PreAuthorize("@perm.hasPerms('system:user:save')")
    @ApiOperation(value = "保存用户信息")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:lock')")
    @ApiOperation(value = "锁定用户信息")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("lock")
    public Result<Void> lock(@RequestBody List<String> username) {
        userService.lock(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:unLock')")
    @ApiOperation(value = "解锁用户")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("unLock")
    public Result<Void> unLock(@RequestBody List<String> username) {
        userService.unLock(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:logOut')")
    @ApiOperation(value = "注销用户")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "用户名, 多个用逗号隔开", name = "username", required = true)
    @PostMapping("logOut")
    public Result<Void> logOut(@RequestBody List<String> username) {
        userService.logOut(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:update')")
    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:updateForSelf')")
    @ApiOperation(value = "修改登录用户信息", notes = "修改登录用户信息")
    @ApiOperationSupport(order = 35)
    @PutMapping("updateForSelf")
    public Result<Void> updateForSelf(@RequestBody UserDTO userDTO) {
        userService.updateForSelf(userDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:uploadAvatar')")
    @ApiOperation(value = "用户头像上传", notes = "用户头像上传")
    @ApiOperationSupport(order = 36)
    @PostMapping("uploadAvatar")
    public Result<String> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return Result.ok(userService.uploadAvatar(file));
    }

    @PreAuthorize("@perm.hasPerms('system:user:resetPassword')")
    @ApiOperation(value = "重置用户密码", notes = "重置用户密码")
    @ApiOperationSupport(order = 40)
    @PutMapping("resetPassword")
    public Result<Void> resetPassword(String username) {
        userService.resetPassword(username);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:updatePassword')")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiOperationSupport(order = 50)
    @PutMapping("updatePassword")
    public Result<Void> updatePassword(@RequestBody UserPasswordDTO userDetailDto) {
        userService.updatePassword(userDetailDto);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:updatePasswordForSelf')")
    @ApiOperation(value = "修改登录人密码", notes = "修改登录人密码")
    @ApiOperationSupport(order = 50)
    @PutMapping("updatePasswordForSelf")
    public Result<Void> updatePasswordForSelf(@RequestBody PasswordDTO passwordDTO) {
        userService.updatePasswordForSelf(passwordDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:user:info')")
    @ApiOperation(value = "根据用户名查询用户信息")
    @ApiOperationSupport(order = 60)
    @ApiImplicitParam(value = "用户名", name = "username", required = true)
    @GetMapping("info")
    public Result<UserDTO> info(String username) {
        return Result.ok(userService.getInfoByUsername(username));
    }

    @PreAuthorize("@perm.hasPerms('system:user:infoForSelf')")
    @ApiOperation(value = "查询登录人用户信息")
    @ApiOperationSupport(order = 60)
    @GetMapping("infoForSelf")
    public Result<UserDTO> info() {
        return Result.ok(userService.getInfo());
    }

    @PreAuthorize("@perm.hasPerms('system:user:detail')")
    @ApiOperation(value = "查询用户角色菜单权限标识信息")
    @ApiOperationSupport(order = 70)
    @GetMapping("detail")
    public Result<UserRoleMenuDtlDTO> detail(String username) {
        return Result.ok(userService.getUserDtl(username));
    }

    @PreAuthorize("@perm.hasPerms('system:user:detailForSelf')")
    @ApiOperation(value = "查询登录人用户角色菜单权限标识信息")
    @ApiOperationSupport(order = 70)
    @GetMapping("detailForSelf")
    public Result<UserRoleMenuDtlDTO> detailForSelf() {
        return Result.ok(userService.getUserDtl());
    }

    @PreAuthorize("@perm.hasPerms('system:user:page')")
    @ApiOperation(value = "分页查询用户信息")
    @ApiOperationSupport(order = 80)
    @PostMapping("page")
    public Result<PageResult<UserDTO>> page(@RequestBody UserQuery query) {
        return Result.ok(userService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:user:bindRole')")
    @ApiOperation(value = "用户绑定角色信息")
    @ApiOperationSupport(order = 90)
    @PostMapping("bindRole")
    public Result<Void> bindRole(@RequestBody UserRoleDTO userRoleDTO) {
        userService.bindRole(userRoleDTO);
        return Result.ok();
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impTemplate')")
    @ApiOperation(value = "导入模板下载", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 100)
    @PostMapping("impTemplate")
    public void impTemplate() throws IOException {
        userService.impTemplate();
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:impExcel')")
    @ApiOperation(value = "导入用户信息", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 100)
    @PostMapping("impExcel")
    public void impExcel(@RequestPart("file") MultipartFile file) throws IOException {
        userService.impExcel(file);
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:user:expExcel')")
    @ApiOperation(value = "导出用户信息", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 110)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody UserQuery query) throws IOException {
        userService.expExcel(query);
    }

}
