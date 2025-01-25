package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.system.service.UserService;
import top.ticho.rainbow.interfaces.dto.ImgCodeEmailDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpOrResetDTO;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.security.constant.BaseOAuth2Const;
import top.ticho.starter.security.dto.Oauth2AccessToken;
import top.ticho.starter.security.handle.LoginUserHandle;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;
import top.ticho.starter.web.util.valid.TiValidUtil;

import javax.annotation.Resource;
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

    @Resource
    private LoginUserHandle loginUserHandle;

    @Resource
    private UserService userService;

    @IgnoreJwtCheck
    @ApiOperation("注册邮箱发送")
    @ApiOperationSupport(order = 10)
    @PostMapping("signUpEmailSend")
    public TiResult<Void> signUpEmailSend(@RequestBody ImgCodeEmailDTO imgCodeEmailDTO) {
        userService.signUpEmailSend(imgCodeEmailDTO);
        return TiResult.ok();
    }

    @IgnoreJwtCheck
    @ApiOperation("注册")
    @ApiOperationSupport(order = 20)
    @PostMapping("signUp")
    public TiResult<UserLoginDTO> signUp(@RequestBody UserSignUpOrResetDTO userSignUpOrResetDTO) {
        return TiResult.ok(userService.signUp(userSignUpOrResetDTO));
    }

    @IgnoreJwtCheck
    @ApiOperation(value = "重置邮箱验证码发送")
    @ApiOperationSupport(order = 30)
    @PostMapping("resetPasswordEmailSend")
    public TiResult<String> resetPasswordEmailSend(@RequestBody ImgCodeEmailDTO imgCodeEmailDTO) {
        return TiResult.ok(userService.resetPasswordEmailSend(imgCodeEmailDTO));
    }

    @IgnoreJwtCheck
    @ApiOperation(value = "重置用户密码")
    @ApiOperationSupport(order = 40)
    @PostMapping("resetPassword")
    public TiResult<UserLoginDTO> resetPassword(@RequestBody UserSignUpOrResetDTO userSignUpOrResetDTO) {
        return TiResult.ok(userService.resetPassword(userSignUpOrResetDTO));
    }

    @IgnoreJwtCheck
    @TiView(ignore = true)
    @ApiOperation(value = "验证码", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 50)
    @GetMapping("imgCode")
    public void imgCode(String imgKey) throws IOException {
        userService.imgCode(imgKey);
    }

    @IgnoreJwtCheck
    @ApiOperation("登录")
    @ApiOperationSupport(order = 60)
    @PostMapping("token")
    public TiResult<Oauth2AccessToken> token(UserLoginDTO userLoginDTO) {
        TiValidUtil.valid(userLoginDTO);
        return TiResult.ok(loginUserHandle.token(userLoginDTO));
    }

    @IgnoreJwtCheck
    @ApiOperation("刷新token")
    @ApiOperationSupport(order = 70)
    @PostMapping("refreshToken")
    public TiResult<Oauth2AccessToken> refreshToken(String refreshToken) {
        return TiResult.ok(loginUserHandle.refreshToken(refreshToken));
    }

    @ApiOperation(value = "token信息查询")
    @ApiOperationSupport(order = 80)
    @GetMapping
    public TiResult<Principal> principal() {
        return TiResult.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @ApiOperation("获取公钥")
    @ApiOperationSupport(order = 90)
    @GetMapping("publicKey")
    public TiResult<String> publicKey() {
        return TiResult.ok(loginUserHandle.publicKey());
    }

}
