package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.dto.command.LoginUserModifyCommand;
import top.ticho.rainbow.application.dto.command.LoginUserModifyPasswordCommand;
import top.ticho.rainbow.application.dto.command.ResetPassworEmailSendCommand;
import top.ticho.rainbow.application.dto.command.ResetPasswordCommand;
import top.ticho.rainbow.application.dto.command.UserSignUpCommand;
import top.ticho.rainbow.application.dto.command.UserSignUpEmailSendCommand;
import top.ticho.rainbow.application.dto.request.LoginDTO;
import top.ticho.rainbow.application.dto.response.LoginUserDTO;
import top.ticho.rainbow.application.dto.response.LoginUserDetailDTO;
import top.ticho.rainbow.application.service.LoginUserService;
import top.ticho.rainbow.application.service.login.DefaultLoginService;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.security.dto.TiToken;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 权限登录
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("oauth")
public class OauthController {
    private final DefaultLoginService defaultLoginService;
    private final LoginUserService loginUserService;

    /**
     * 用户注册邮箱发送
     */
    @IgnoreJwtCheck
    @PostMapping("sign-up/email/send")
    public TiResult<Void> signUpEmailSend(@Validated @RequestBody UserSignUpEmailSendCommand userSignUpEmailSendCommand) {
        loginUserService.signUpEmailSend(userSignUpEmailSendCommand);
        return TiResult.ok();
    }

    /**
     * 用户注册
     */
    @IgnoreJwtCheck
    @PostMapping("sign-up")
    public TiResult<LoginDTO> signUp(@Validated @RequestBody UserSignUpCommand userSignUpCommand) {
        return TiResult.ok(loginUserService.signUp(userSignUpCommand));
    }

    /**
     * 重置用户密码-邮箱验证码发送
     */
    @IgnoreJwtCheck
    @PostMapping("reset-password/email/send")
    public TiResult<String> resetPasswordEmailSend(@Validated @RequestBody ResetPassworEmailSendCommand resetPassworEmailSendCommand) {
        return TiResult.ok(loginUserService.resetPasswordEmailSend(resetPassworEmailSendCommand));
    }

    /**
     * 重置用户密码
     */
    @IgnoreJwtCheck
    @PatchMapping("password/reset")
    public TiResult<LoginDTO> resetPassword(@Validated @RequestBody ResetPasswordCommand resetPasswordCommand) {
        return TiResult.ok(loginUserService.resetPassword(resetPasswordCommand));
    }

    /**
     * 验证码
     */
    @IgnoreJwtCheck
    @TiView(ignore = true)
    @GetMapping("img-code")
    public void imgCode(@NotBlank(message = "验证码秘钥不能为空") String imgKey) throws IOException {
        loginUserService.imgCode(imgKey);
    }

    /**
     * 登录
     */
    @IgnoreJwtCheck
    @PostMapping("token")
    public TiResult<TiToken> token(@Validated LoginDTO loginDTO) {
        return TiResult.ok(defaultLoginService.token(loginDTO));
    }

    /**
     * 刷新token
     */
    @IgnoreJwtCheck
    @PostMapping("token/refresh")
    public TiResult<TiToken> refreshToken(@NotBlank(message = "refreshToken不能为空") String refreshToken) {
        return TiResult.ok(defaultLoginService.refreshToken(refreshToken));
    }

    /**
     * 查询登录用户信息
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.OAUTH_USER_FIND + "')")
    @GetMapping("user")
    public TiResult<LoginUserDTO> find() {
        return TiResult.ok(loginUserService.find());
    }

    /**
     * 查询登录用户详细信息
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.OAUTH_USER_DETAIL_FIND + "')")
    @GetMapping("user/detail")
    public TiResult<LoginUserDetailDTO> findDetail() {
        return TiResult.ok(loginUserService.findDetail());
    }

    /**
     * 上传登录用户头像
     *
     * @param file 文件
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.OAUTH_USER_AVATAR_UPLOAD + "')")
    @PostMapping("user/avatar/upload")
    public TiResult<String> uploadAvatar(@NotNull(message = "请上传头像") MultipartFile file) {
        return TiResult.ok(loginUserService.uploadAvatar(file));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.OAUTH_USER_MODIFY + "')")
    @PutMapping("user")
    public TiResult<Void> modify(@Validated @RequestBody LoginUserModifyCommand loginUserModifyCommand) {
        loginUserService.modify(loginUserModifyCommand);
        return TiResult.ok();
    }

    /**
     * 修改用户密码
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.OAUTH_USER_PASSWORD_MODIFY + "')")
    @PatchMapping("user/password")
    public TiResult<Void> modifyPassword(@Validated @RequestBody LoginUserModifyPasswordCommand loginUserModifyPasswordCommand) {
        loginUserService.modifyPassword(loginUserModifyPasswordCommand);
        return TiResult.ok();
    }

}
