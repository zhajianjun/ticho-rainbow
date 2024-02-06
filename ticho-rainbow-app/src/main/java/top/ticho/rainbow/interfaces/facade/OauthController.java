package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.security.annotation.IgnoreJwtCheck;
import top.ticho.boot.security.constant.BaseOAuth2Const;
import top.ticho.boot.security.dto.Oauth2AccessToken;
import top.ticho.boot.security.handle.LoginUserHandle;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.web.annotation.View;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpDTO;

import java.io.IOException;
import java.security.Principal;

/**
 * 权限用户登录
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController(BaseOAuth2Const.OAUTH2_CONTROLLER)
@RequestMapping("oauth")
@ApiSort(20)
@Api(tags = "权限登录")
public class OauthController {

    @Autowired
    private LoginUserHandle loginUserHandle;

    @Autowired
    private UserService userService;

    @IgnoreJwtCheck
    @ApiOperation("注册")
    @ApiOperationSupport(order = 10)
    @PostMapping("signUp")
    public Result<Void> signUp(@RequestBody UserSignUpDTO userSignUpDTO) {
        userService.signUp(userSignUpDTO);
        return Result.ok();
    }

    @IgnoreJwtCheck
    @ApiOperation("注册邮箱发送")
    @ApiOperationSupport(order = 11)
    @PostMapping("signUpEmailSend")
    public Result<Void> signUpEmailSend(String email) {
        userService.signUpEmailSend(email);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('login:oauth:confirm')")
    @ApiOperation(value = "用户注册确认")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "账户", name = "username", required = true)
    @PutMapping("confirm")
    public Result<Void> confirm(String username) {
        userService.confirm(username);
        return Result.ok();
    }

    @IgnoreJwtCheck
    @ApiOperation("登录")
    @ApiOperationSupport(order = 30)
    @PostMapping("token")
    public Result<Oauth2AccessToken> token(UserLoginDTO userLoginDTO) {
        ValidUtil.valid(userLoginDTO);
        return Result.ok(loginUserHandle.token(userLoginDTO));
    }

    @IgnoreJwtCheck
    @View(ignore = true)
    @ApiOperation(value = "登录验证码", notes = "登录验证码", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 80)
    @GetMapping("imgCode")
    public void imgCode(String imgKey) throws IOException {
        userService.imgCode(imgKey);
    }

    @IgnoreJwtCheck
    @ApiOperation("刷新token")
    @ApiOperationSupport(order = 40)
    @PostMapping("refreshToken")
    public Result<Oauth2AccessToken> refreshToken(String refreshToken) {
        return Result.ok(loginUserHandle.refreshToken(refreshToken));
    }

    @ApiOperation(value = "token信息查询")
    @ApiOperationSupport(order = 50)
    @GetMapping
    public Result<Principal> principal() {
        return Result.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @ApiOperation("获取公钥")
    @ApiOperationSupport(order = 60)
    @GetMapping("publicKey")
    public Result<String> publicKey() {
        return Result.ok(loginUserHandle.publicKey());
    }

}
