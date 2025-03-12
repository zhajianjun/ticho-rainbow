package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.command.ResetPassworEmailSendCommand;
import top.ticho.rainbow.application.dto.command.ResetPasswordCommand;
import top.ticho.rainbow.application.dto.command.SignUpEmailSendCommand;
import top.ticho.rainbow.application.dto.request.UserLoginDTO;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.security.constant.TiSecurityConst;
import top.ticho.starter.security.dto.TiToken;
import top.ticho.starter.security.handle.LoginUserHandle;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import java.io.IOException;
import java.security.Principal;

/**
 * 权限登录
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@RestController(TiSecurityConst.OAUTH2_CONTROLLER)
@RequestMapping("oauth")
public class OauthController {
    private final LoginUserHandle loginUserHandle;    private final UserService userService;
    /**
     * 注册邮箱发送
     */
    @IgnoreJwtCheck
    @PostMapping("signUpEmailSend")
    public TiResult<Void> signUpEmailSend(@Validated @RequestBody SignUpEmailSendCommand signUpEmailSendCommand) {
        userService.signUpEmailSend(signUpEmailSendCommand);
        return TiResult.ok();
    }

    /**
     * 注册
     */
    @IgnoreJwtCheck
    @PostMapping("signUp")
    public TiResult<UserLoginDTO> signUp(@RequestBody ResetPasswordCommand userSignUpOrResetDTO) {
        return TiResult.ok(userService.signUp(userSignUpOrResetDTO));
    }

    /**
     * 重置邮箱验证码发送
     *
     * @param resetPassworEmailSendCommand IMG 代码电子邮件 DTO
     * @return {@link TiResult }<{@link String }>
     */
    @IgnoreJwtCheck
    @PostMapping("resetPasswordEmailSend")
    public TiResult<String> resetPasswordEmailSend(@Validated @RequestBody ResetPassworEmailSendCommand resetPassworEmailSendCommand) {
        return TiResult.ok(userService.resetPasswordEmailSend(resetPassworEmailSendCommand));
    }

    /**
     * 重置用户密码
     */
    @IgnoreJwtCheck
    @PostMapping("resetPassword")
    public TiResult<UserLoginDTO> resetPassword(@Validated @RequestBody ResetPasswordCommand resetPasswordCommand) {
        return TiResult.ok(userService.resetPassword(resetPasswordCommand));
    }

    /**
     * 验证码
     */
    @IgnoreJwtCheck
    @TiView(ignore = true)
    @GetMapping("imgCode")
    public void imgCode(String imgKey) throws IOException {
        userService.imgCode(imgKey);
    }

    /**
     * 登录
     */
    @IgnoreJwtCheck
    @PostMapping("token")
    public TiResult<TiToken> token(@Validated UserLoginDTO userLoginDTO) {
        return TiResult.ok(loginUserHandle.token(userLoginDTO));
    }

    /**
     * 刷新token
     */
    @IgnoreJwtCheck
    @PostMapping("refreshToken")
    public TiResult<TiToken> refreshToken(String refreshToken) {
        return TiResult.ok(loginUserHandle.refreshToken(refreshToken));
    }

    /**
     * token信息查询
     */
    @GetMapping("principal")
    public TiResult<Principal> principal() {
        return TiResult.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 获取公钥
     *
     * @return {@link TiResult }<{@link String }>
     */
    @GetMapping("publicKey")
    public TiResult<String> publicKey() {
        return TiResult.ok(loginUserHandle.publicKey());
    }

}
