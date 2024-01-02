package top.ticho.intranet.server.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import top.ticho.boot.security.constant.BaseOAuth2Const;
import top.ticho.boot.security.dto.BaseLoginRequest;
import top.ticho.boot.security.dto.Oauth2AccessToken;
import top.ticho.boot.security.handle.LoginUserHandle;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.intranet.server.application.service.UserService;
import top.ticho.intranet.server.interfaces.dto.UserDTO;
import top.ticho.intranet.server.interfaces.dto.UserPassworUpdDTO;
import top.ticho.intranet.server.interfaces.query.UserQuery;

import java.security.Principal;

/**
 * 用户信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@RestController(BaseOAuth2Const.OAUTH2_CONTROLLER)
@RequestMapping("user")
@ApiSort(Ordered.HIGHEST_PRECEDENCE + 200)
@Api(tags = "用户信息")
public class UserController {

    @Autowired
    private LoginUserHandle loginUserHandle;

    @Autowired
    private UserService userService;

    @ApiOperation("登录")
    @ApiOperationSupport(order = 10)
    @PostMapping("token")
    public Result<Oauth2AccessToken> token(@RequestBody BaseLoginRequest loginRequest) {
        return Result.ok(loginUserHandle.token(loginRequest));
    }

    @ApiOperation("刷新token")
    @ApiOperationSupport(order = 20)
    @PostMapping("refreshToken")
    public Result<Oauth2AccessToken> refreshToken(String refreshToken) {
        return Result.ok(loginUserHandle.refreshToken(refreshToken));
    }

    @ApiOperation(value = "用户信息查询")
    @ApiOperationSupport(order = 30)
    @GetMapping("principal")
    public Result<Principal> principal() {
        return Result.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @ApiOperation(value = "保存用户信息")
    @ApiOperationSupport(order = 40)
    @PostMapping
    public Result<Void> save(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户信息")
    @ApiOperationSupport(order = 50)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> remove(Long id) {
        userService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户信息")
    @ApiOperationSupport(order = 60)
    @PutMapping
    public Result<Void> update(@RequestBody UserDTO userDTO) {
        userService.updateById(userDTO);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户密码信息")
    @ApiOperationSupport(order = 60)
    @PutMapping("updatePassword")
    public Result<Void> updatePassword(@RequestBody UserPassworUpdDTO userPassworUpdDTO) {
        userService.updatePassword(userPassworUpdDTO);
        return Result.ok();
    }

    @ApiOperation(value = "主键查询用户信息")
    @ApiOperationSupport(order = 70)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<UserDTO> getById(Long id) {
        return Result.ok(userService.getById(id));
    }

    @ApiOperation(value = "分页查询用户信息")
    @ApiOperationSupport(order = 80)
    @GetMapping("page")
    public Result<PageResult<UserDTO>> page(UserQuery query) {
        return Result.ok(userService.page(query));
    }

}
