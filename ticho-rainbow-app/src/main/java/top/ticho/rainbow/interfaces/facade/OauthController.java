package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import top.ticho.rainbow.interfaces.dto.ImgCodeEmailDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpOrResetDTO;

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
    @View(ignore = true)
    @ApiOperation(value = "验证码", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 10)
    @GetMapping("imgCode")
    public void imgCode(String imgKey) throws IOException {
        userService.imgCode(imgKey);
    }

    @IgnoreJwtCheck
    @ApiOperation("注册邮箱发送")
    @ApiOperationSupport(order = 20)
    @PostMapping("signUpEmailSend")
    public Result<Void> signUpEmailSend(@RequestBody ImgCodeEmailDTO imgCodeEmailDTO) {
        userService.signUpEmailSend(imgCodeEmailDTO);
        return Result.ok();
    }

    @IgnoreJwtCheck
    @ApiOperation("注册")
    @ApiOperationSupport(order = 30)
    @PostMapping("signUp")
    public Result<UserLoginDTO> signUp(@RequestBody UserSignUpOrResetDTO userSignUpOrResetDTO) {
        return Result.ok(userService.signUp(userSignUpOrResetDTO));
    }


    @IgnoreJwtCheck
    @ApiOperation(value = "重置邮箱验证码发送")
    @ApiOperationSupport(order = 40)
    @PostMapping("resetPasswordEmailSend")
    public Result<String> resetPasswordEmailSend(@RequestBody ImgCodeEmailDTO imgCodeEmailDTO) {
        return Result.ok(userService.resetPasswordEmailSend(imgCodeEmailDTO));
    }

    @IgnoreJwtCheck
    @ApiOperation(value = "重置用户密码")
    @ApiOperationSupport(order = 50)
    @PostMapping("resetPassword")
    public Result<UserLoginDTO> resetPassword(@RequestBody UserSignUpOrResetDTO userSignUpOrResetDTO) {
        return Result.ok(userService.resetPassword(userSignUpOrResetDTO));
    }

    @IgnoreJwtCheck
    @ApiOperation("登录")
    @ApiOperationSupport(order = 60)
    @PostMapping("token")
    public Result<Oauth2AccessToken> token(UserLoginDTO userLoginDTO) {
        ValidUtil.valid(userLoginDTO);
        return Result.ok(loginUserHandle.token(userLoginDTO));
    }

    @IgnoreJwtCheck
    @ApiOperation("刷新token")
    @ApiOperationSupport(order = 70)
    @PostMapping("refreshToken")
    public Result<Oauth2AccessToken> refreshToken(String refreshToken) {
        return Result.ok(loginUserHandle.refreshToken(refreshToken));
    }

    @ApiOperation(value = "token信息查询")
    @ApiOperationSupport(order = 80)
    @GetMapping
    public Result<Principal> principal() {
        return Result.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @ApiOperation("获取公钥")
    @ApiOperationSupport(order = 90)
    @GetMapping("publicKey")
    public Result<String> publicKey() {
        return Result.ok(loginUserHandle.publicKey());
    }

}
