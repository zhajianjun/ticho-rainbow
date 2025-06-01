package top.ticho.rainbow.application.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.assembler.FileInfoAssembler;
import top.ticho.rainbow.application.assembler.UserAssembler;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.command.FileUploadCommand;
import top.ticho.rainbow.application.dto.command.LoginUserModifyCommand;
import top.ticho.rainbow.application.dto.command.LoginUserModifyPasswordCommand;
import top.ticho.rainbow.application.dto.command.ResetPassworEmailSendCommand;
import top.ticho.rainbow.application.dto.command.ResetPasswordCommand;
import top.ticho.rainbow.application.dto.command.UserSignUpCommand;
import top.ticho.rainbow.application.dto.command.UserSignUpEmailSendCommand;
import top.ticho.rainbow.application.dto.request.LoginDTO;
import top.ticho.rainbow.application.dto.response.FileInfoDTO;
import top.ticho.rainbow.application.dto.response.LoginUserDTO;
import top.ticho.rainbow.application.dto.response.LoginUserDetailDTO;
import top.ticho.rainbow.application.executor.FileInfoExecutor;
import top.ticho.rainbow.application.executor.TemplateExecutor;
import top.ticho.rainbow.application.executor.UserExecutor;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.entity.vo.UserModifyVO;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.util.UserUtil;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.mail.component.TiMailContent;
import top.ticho.starter.mail.component.TiMailInines;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.exception.TiBizException;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.file.TiMultipartFile;
import top.ticho.tool.json.util.TiJsonUtil;

import jakarta.servlet.http.HttpServletResponse;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 用户信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class LoginUserService {
    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final UserRoleRepository userRoleRepository;
    private final HttpServletResponse response;
    private final RoleRepository roleRepository;
    private final TiCacheTemplate tiCacheTemplate;
    private final EmailRepository emailRepository;
    private final FileInfoExecutor fileInfoExecutor;
    private final FileInfoAssembler fileInfoAssembler;
    private final UserExecutor userExecutor;
    private final PasswordEncoder passwordEncoder;
    private final TemplateExecutor templateExecutor;

    public void imgCode(String imgKey) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (OutputStream out = response.getOutputStream()) {
            LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
            gifCaptcha.createCode();
            String code = gifCaptcha.getCode();
            tiCacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
            BufferedImage buffImg = gifCaptcha.getImage();
            ImageIO.write(buffImg, "png", out);
        } catch (Exception e) {
            log.error("获取验证码失败，error {}", e.getMessage(), e);
            String message = e.getMessage();
            int code = TiBizErrCode.FAIL.getCode();
            if (e instanceof TiBizException TiBizException) {
                code = TiBizException.getCode();
                message = TiBizException.getMsg();
            }
            TiResult<String> result = TiResult.of(code, message, null);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(TiJsonUtil.toJsonString(result));
        }
    }

    public void imgCodeValid(String key, String imgCode) {
        userExecutor.imgCodeValid(key, imgCode);
    }

    @Transactional(rollbackFor = Exception.class)
    public void signUpEmailSend(UserSignUpEmailSendCommand userSignUpEmailSendCommand) {
        // 图片验证码校验
        imgCodeValid(userSignUpEmailSendCommand.getImgKey(), userSignUpEmailSendCommand.getImgCode());
        // 发送邮箱验证码
        String email = userSignUpEmailSendCommand.getEmail();
        User user = userRepository.findByEmail(email);
        TiAssert.isNull(user, "用户已存在");
        String code = tiCacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        TiAssert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        tiCacheTemplate.put(CacheConst.SIGN_UP_CODE, email, code);
        TiMailInines mailInines = new TiMailInines();
        mailInines.setContentId("p01");
        mailInines.setFile(new TiMultipartFile("captcha", "captcha.png", MediaType.IMAGE_PNG_VALUE, gifCaptcha.getImageBytes()));
        TiMailContent mailContent = new TiMailContent();
        mailContent.setTo(email);
        mailContent.setSubject("注册");
        mailContent.setContent(templateExecutor.render("/template/signUpEmailSend.html"));
        mailContent.setInlines(Collections.singletonList(mailInines));
        boolean sendMail = emailRepository.sendMail(mailContent);
        TiAssert.isTrue(sendMail, "发送邮件失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public LoginDTO signUp(UserSignUpCommand userSignUpCommand) {
        String email = userSignUpCommand.getEmail();
        String cacheEmailCode = tiCacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        TiAssert.isNotBlank(cacheEmailCode, "验证码已过期");
        tiCacheTemplate.evict(CacheConst.SIGN_UP_CODE, email);
        TiAssert.isTrue(userSignUpCommand.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        userSignUpCommand.setPassword(userExecutor.encodePassword(userSignUpCommand.getPassword()));
        User user = userAssembler.toEntity(userSignUpCommand);
        userExecutor.checkRepeat(user);
        TiAssert.isTrue(userRepository.save(user), "注册失败");
        Role guestRole = roleRepository.getGuestRole();
        TiAssert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        userRoleRepository.removeAndSave(user.getId(), Collections.singletonList(guestRole.getId()));
        // 返回登录使用参数
        return toLoginDTO(user.getUsername());
    }

    @Transactional(rollbackFor = Exception.class)
    public String resetPasswordEmailSend(ResetPassworEmailSendCommand resetPassworEmailSendCommand) {
        imgCodeValid(resetPassworEmailSendCommand.getImgKey(), resetPassworEmailSendCommand.getImgCode());
        // 发送邮箱验证码
        String email = resetPassworEmailSendCommand.getEmail();
        User dbUser = userRepository.findByEmail(email);
        TiAssert.isNotNull(dbUser, "用户不存在");
        String code = tiCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        TiAssert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        tiCacheTemplate.put(CacheConst.RESET_PASSWORD_CODE, email, code);
        TiMailInines mailInines = new TiMailInines();
        mailInines.setContentId("p01");
        mailInines.setFile(new TiMultipartFile("captcha", "captcha.png", MediaType.IMAGE_PNG_VALUE, gifCaptcha.getImageBytes()));
        TiMailContent mailContent = new TiMailContent();
        mailContent.setTo(email);
        mailContent.setSubject("重置密码");
        mailContent.setContent(templateExecutor.render("/template/resetPasswordEmailSend.html"));
        mailContent.setInlines(Collections.singletonList(mailInines));
        boolean sendMail = emailRepository.sendMail(mailContent);
        TiAssert.isTrue(sendMail, "发送邮件失败");
        return dbUser.getUsername();
    }

    public LoginDTO resetPassword(ResetPasswordCommand resetPasswordCommand) {
        String email = resetPasswordCommand.getEmail();
        String cacheEmailCode = tiCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        TiAssert.isNotBlank(cacheEmailCode, "验证码已过期");
        tiCacheTemplate.evict(CacheConst.RESET_PASSWORD_CODE, email);
        TiAssert.isTrue(resetPasswordCommand.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        User user = userRepository.findByEmail(email);
        TiAssert.isNotNull(
            user, () -> {
                log.info("重置用户{}密码失败，用户不存在", email);
                return "重置失败";
            }
        );
        String username = user.getUsername();
        String encodedPasswordNew = passwordEncoder.encode(resetPasswordCommand.getPassword());
        user.modifyPassword(encodedPasswordNew);
        // 更新密码
        boolean modify = userRepository.modify(user);
        TiAssert.isTrue(modify, "更新密码失败");
        // 返回登录使用参数
        return toLoginDTO(username);
    }

    /**
     * 返回登录使用参数
     */
    private LoginDTO toLoginDTO(String username) {
        String imgKey = IdUtil.fastSimpleUUID();
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        String code = gifCaptcha.getCode();
        tiCacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setImgKey(imgKey);
        loginDTO.setImgCode(code);
        return loginDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        User user = userRepository.findByUsername(UserUtil.getCurrentUsername());
        TiAssert.isNotNull(user, "头像上传失败, 用户不存在");
        FileUploadCommand fileUploadCommand = new FileUploadCommand();
        fileUploadCommand.setFile(file);
        fileUploadCommand.setType(1);
        fileUploadCommand.setRelativePath(StrUtil.format("user/{}/avatar", user.getUsername()));
        fileUploadCommand.setRemark("头像");
        FileInfoDTO upload = fileInfoExecutor.upload(fileUploadCommand);
        user.modifyPhoto(upload.getId().toString());
        userRepository.modify(user);
        FileInfo fileInfo = fileInfoAssembler.toEntity(upload);
        return fileInfoExecutor.presigned(fileInfo, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(LoginUserModifyCommand loginUserModifyCommand) {
        User user = userRepository.findByUsername(UserUtil.getCurrentUsername());
        TiAssert.isNotNull(user, "修改失败, 用户不存在");
        user.checkVersion(loginUserModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        UserModifyVO modifyVo = userAssembler.toModifyVo(loginUserModifyCommand);
        List<String> errors = userExecutor.checkRepeat(user);
        TiAssert.isEmpty(errors, () -> errors.get(0));
        user.modify(modifyVo);
        TiAssert.isTrue(userRepository.modify(user), "修改失败，请刷新后重试");
    }


    public LoginUserDTO find() {
        User user = userRepository.findCacheByUsername(UserUtil.getCurrentUsername());
        LoginUserDTO loginUserDTO = userAssembler.toLoginUserDTO(user);
        setPhoto(loginUserDTO.getPhoto(), loginUserDTO::setPhoto);
        return loginUserDTO;
    }

    public LoginUserDetailDTO findDetail() {
        User user = userRepository.findCacheByUsername(UserUtil.getCurrentUsername());
        LoginUserDetailDTO loginUserDetailDTO = userAssembler.toLoginUserDetailDTO(user);
        setPhoto(loginUserDetailDTO.getPhoto(), loginUserDetailDTO::setPhoto);
        return loginUserDetailDTO;
    }

    public void setPhoto(String photo, Consumer<String> setPhoto) {
        if (!StrUtil.isNumeric(photo)) {
            return;
        }
        Long fileId = Long.parseLong(photo);
        String url = fileInfoExecutor.presigned(fileId, null, true);
        setPhoto.accept(url);
    }

    public void modifyPassword(LoginUserModifyPasswordCommand loginUserModifyPasswordCommand) {
        SecurityUser loginUser = UserUtil.getCurrentUser();
        User user = userRepository.findByUsername(loginUser.getUsername());
        TiAssert.isNotEmpty(user, "用户不存在");
        user.checkVersion(loginUserModifyPasswordCommand.getVersion(), "数据已被修改，请刷新后重试");
        userExecutor.modifyPassword(loginUserModifyPasswordCommand.getPassword(), loginUserModifyPasswordCommand.getNewPassword(), user);
    }

}
