package top.ticho.rainbow.application.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.assembler.FileInfoAssembler;
import top.ticho.rainbow.application.assembler.RoleAssembler;
import top.ticho.rainbow.application.assembler.UserAssembler;
import top.ticho.rainbow.application.dto.command.UserModifySelfPasswordCommand;
import top.ticho.rainbow.application.dto.response.RoleDTO;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.response.UserDTO;
import top.ticho.rainbow.application.dto.command.UserModifyPasswordCommand;
import top.ticho.rainbow.application.dto.command.UserBindRoleCommand;
import top.ticho.rainbow.application.dto.command.FileUploadCommand;
import top.ticho.rainbow.application.dto.command.ResetPassworEmailSendCommand;
import top.ticho.rainbow.application.dto.command.ResetPasswordCommand;
import top.ticho.rainbow.application.dto.command.SignUpEmailSendCommand;
import top.ticho.rainbow.application.dto.command.UseModifyCommand;
import top.ticho.rainbow.application.dto.command.UseModifySelfCommand;
import top.ticho.rainbow.application.dto.command.UseSaveCommand;
import top.ticho.rainbow.application.dto.excel.UserExp;
import top.ticho.rainbow.application.dto.excel.UserImp;
import top.ticho.rainbow.application.dto.excel.UserImpModel;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.application.dto.request.UserLoginDTO;
import top.ticho.rainbow.application.dto.response.FileInfoDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.entity.UserRole;
import top.ticho.rainbow.domain.entity.vo.UserModifyVO;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.constant.CommConst;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.core.enums.UserStatus;
import top.ticho.rainbow.infrastructure.core.util.BeetlUtil;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.mail.component.TiMailContent;
import top.ticho.starter.mail.component.TiMailInines;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.exception.TiBizException;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.file.TiMultipartFile;
import top.ticho.starter.web.util.TiSpringUtil;
import top.ticho.starter.web.util.valid.TiValidUtil;
import top.ticho.tool.json.util.TiJsonUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserService extends AbstractAuthServiceImpl {
    private final UserRepository userRepository;
    private final UserAssembler userAssembler;
    private final RoleAssembler roleAssembler;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final HttpServletResponse response;
    private final RoleRepository roleRepository;
    private final TiCacheTemplate springCacheTemplate;
    private final EmailRepository emailRepository;
    private final FileInfoService fileInfoService;
    private final FileInfoAssembler fileInfoAssembler;
    private final DictExecutor dictExecutor;

    public void imgCode(String imgKey) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (OutputStream out = response.getOutputStream()) {
            LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
            gifCaptcha.createCode();
            String code = gifCaptcha.getCode();
            springCacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
            BufferedImage buffImg = gifCaptcha.getImage();
            ImageIO.write(buffImg, "png", out);
        } catch (Exception e) {
            log.error("获取验证码失败，error {}", e.getMessage(), e);
            String message = e.getMessage();
            int code = TiBizErrCode.FAIL.getCode();
            if (e instanceof TiBizException) {
                TiBizException TiBizException = ((TiBizException) e);
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
        String cacheImgCode = springCacheTemplate.get(CacheConst.VERIFY_CODE, key, String.class);
        TiAssert.isNotBlank(cacheImgCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.VERIFY_CODE, key);
        TiAssert.isTrue(imgCode.equalsIgnoreCase(cacheImgCode), "验证码不正确");
    }

    @Transactional(rollbackFor = Exception.class)
    public void signUpEmailSend(SignUpEmailSendCommand signUpEmailSendCommand) {
        // 图片验证码校验
        imgCodeValid(signUpEmailSendCommand.getImgKey(), signUpEmailSendCommand.getImgCode());
        // 发送邮箱验证码
        String email = signUpEmailSendCommand.getEmail();
        User dbUser = userRepository.getByEmail(email);
        TiAssert.isNull(dbUser, "用户已存在");
        String code = springCacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        TiAssert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        springCacheTemplate.put(CacheConst.SIGN_UP_CODE, email, code);
        GroupTemplate groupTemplate = BeetlUtil.getGroupTemplate(true);
        Template template = groupTemplate.getTemplate("/template/signUpEmailSend.html");
        TiMailInines mailInines = new TiMailInines();
        mailInines.setContentId("p01");
        mailInines.setFile(new TiMultipartFile("captcha", "captcha.png", MediaType.IMAGE_PNG_VALUE, gifCaptcha.getImageBytes()));
        TiMailContent mailContent = new TiMailContent();
        mailContent.setTo(email);
        mailContent.setSubject("注册");
        mailContent.setContent(template.render());
        mailContent.setInlines(Collections.singletonList(mailInines));
        boolean sendMail = emailRepository.sendMail(mailContent);
        TiAssert.isTrue(sendMail, "发送邮件失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public UserLoginDTO signUp(ResetPasswordCommand resetPasswordCommand) {
        TiValidUtil.valid(resetPasswordCommand);
        String email = resetPasswordCommand.getEmail();
        String cacheEmailCode = springCacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        TiAssert.isNotBlank(cacheEmailCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.SIGN_UP_CODE, email);
        TiAssert.isTrue(resetPasswordCommand.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        String username = resetPasswordCommand.getUsername();
        String password = resetPasswordCommand.getPassword();
        User.UserBuilder builder = User.builder();
        builder.username(username);
        builder.nickname(username);
        builder.username(email);
        builder.password(passwordEncoder.encode(password));
        builder.status(UserStatus.NORMAL.code());
        User user = builder.build();
        checkRepeat(user);
        TiAssert.isTrue(userRepository.save(user), TiBizErrCode.FAIL, "注册失败");
        Role guestRole = roleRepository.getGuestRole();
        TiAssert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        userRoleRepository.removeAndSave(user.getId(), Collections.singletonList(guestRole.getId()));
        // 返回登录使用参数
        return getUserLoginDTO(username);
    }

    @Transactional(rollbackFor = Exception.class)
    public String resetPasswordEmailSend(ResetPassworEmailSendCommand resetPassworEmailSendCommand) {
        imgCodeValid(resetPassworEmailSendCommand.getImgKey(), resetPassworEmailSendCommand.getImgCode());
        // 发送邮箱验证码
        String email = resetPassworEmailSendCommand.getEmail();
        User dbUser = userRepository.getByEmail(email);
        TiAssert.isNotNull(dbUser, "用户不存在");
        String code = springCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        TiAssert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        springCacheTemplate.put(CacheConst.RESET_PASSWORD_CODE, email, code);
        GroupTemplate groupTemplate = BeetlUtil.getGroupTemplate(true);
        Template template = groupTemplate.getTemplate("/template/resetPasswordEmailSend.html");
        TiMailInines mailInines = new TiMailInines();
        mailInines.setContentId("p01");
        mailInines.setFile(new TiMultipartFile("captcha", "captcha.png", MediaType.IMAGE_PNG_VALUE, gifCaptcha.getImageBytes()));
        TiMailContent mailContent = new TiMailContent();
        mailContent.setTo(email);
        mailContent.setSubject("重置密码");
        mailContent.setContent(template.render());
        mailContent.setInlines(Collections.singletonList(mailInines));
        boolean sendMail = emailRepository.sendMail(mailContent);
        TiAssert.isTrue(sendMail, "发送邮件失败");
        return dbUser.getUsername();
    }

    public UserLoginDTO resetPassword(ResetPasswordCommand resetPasswordCommand) {
        String email = resetPasswordCommand.getEmail();
        String cacheEmailCode = springCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        TiAssert.isNotBlank(cacheEmailCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.RESET_PASSWORD_CODE, email);
        TiAssert.isTrue(resetPasswordCommand.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        User user = userRepository.getByEmail(email);
        TiAssert.isNotNull(
            user, TiBizErrCode.FAIL, () -> {
                log.info("重置用户{}密码失败，用户不存在", email);
                return "重置失败";
            }
        );
        String username = user.getUsername();
        String encodedPasswordNew = passwordEncoder.encode(resetPasswordCommand.getPassword());
        user.modifyPassword(encodedPasswordNew);
        // 更新密码
        boolean modify = userRepository.modify(user);
        TiAssert.isTrue(modify, TiBizErrCode.FAIL, "更新密码失败");
        // 返回登录使用参数
        return getUserLoginDTO(username);
    }

    public void resetPassword(String username) {
        boolean admin = UserUtil.isAdmin();
        TiAssert.isTrue(admin, TiBizErrCode.FAIL, "无管理员操作权限");
        TiAssert.isTrue(!SecurityConst.ADMIN.equals(username), TiBizErrCode.FAIL, "管理员账户无法重置密码");
        User user = userRepository.getByUsername(username);
        TiAssert.isNotNull(user, "用户不存在");
        String encodedPasswordNew = passwordEncoder.encode(CommConst.DEFAULT_PASSWORD);
        user.modifyPassword(encodedPasswordNew);
        TiAssert.isTrue(userRepository.modify(user), TiBizErrCode.FAIL, "重置密码失败");
    }

    /**
     * 返回登录使用参数
     */
    private UserLoginDTO getUserLoginDTO(String username) {
        String imgKey = IdUtil.fastSimpleUUID();
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        String code = gifCaptcha.getCode();
        springCacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUsername(username);
        userLoginDTO.setImgKey(imgKey);
        userLoginDTO.setImgCode(code);
        return userLoginDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(UseSaveCommand useSaveCommand) {
        String password = useSaveCommand.getPassword();
        useSaveCommand.setPassword(passwordEncoder.encode(password));
        User user = userAssembler.toEntity(useSaveCommand);
        checkRepeat(user);
        TiAssert.isTrue(userRepository.save(user), TiBizErrCode.FAIL, "保存失败");
        if (CollUtil.isEmpty(useSaveCommand.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), useSaveCommand.getRoleIds());
    }

    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        User user = userRepository.getByUsername(UserUtil.getCurrentUsername());
        TiAssert.isNotNull(user, TiBizErrCode.FAIL, "头像上传失败, 用户不存在");
        FileUploadCommand fileUploadCommand = new FileUploadCommand();
        fileUploadCommand.setFile(file);
        fileUploadCommand.setType(1);
        fileUploadCommand.setRelativePath(StrUtil.format("user/{}/avatar", user.getUsername()));
        fileUploadCommand.setRemark("头像");
        FileInfoDTO upload = fileInfoService.upload(fileUploadCommand);
        user.modifyPhoto(upload.getId().toString());
        userRepository.modify(user);
        FileInfo fileInfo = fileInfoAssembler.toEntity(upload);
        return fileInfoService.getUrl(fileInfo, null, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(UseModifyCommand useModifyCommand) {
        User user = userRepository.find(useModifyCommand.getId());
        TiAssert.isNotNull(user, TiBizErrCode.FAIL, "修改失败, 用户不存在");
        UserModifyVO modifyVo = userAssembler.toModifyVo(useModifyCommand);
        modify(user, modifyVo);
        userRoleRepository.removeAndSave(useModifyCommand.getId(), useModifyCommand.getRoleIds());
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyForSelf(UseModifySelfCommand useModifySelfCommand) {
        User user = userRepository.getByUsername(UserUtil.getCurrentUsername());
        TiAssert.isNotNull(user, TiBizErrCode.FAIL, "修改失败, 用户不存在");
        UserModifyVO modifyVo = userAssembler.toModifyVo(useModifySelfCommand);
        modify(user, modifyVo);
    }

    private void modify(User user, UserModifyVO modifyVo) {
        List<String> errors = checkRepeat(user);
        TiAssert.isEmpty(errors, TiBizErrCode.FAIL, () -> errors.get(0));
        user.modify(modifyVo);
        TiAssert.isTrue(userRepository.modify(user), TiBizErrCode.FAIL, "修改失败");
    }

    public UserDTO getInfoByUsername(String username) {
        User user = userRepository.getCacheByUsername(username);
        UserDTO userDTO = userAssembler.toDTO(user);
        Optional.ofNullable(userDTO).ifPresent(x -> setRoles(Collections.singletonList(x)));
        return userDTO;
    }

    public UserDTO getInfo() {
        return getInfoByUsername(UserUtil.getCurrentUsername());
    }

    public TiPageResult<UserDTO> page(UserQuery query) {
        query.checkPage();
        Page<User> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        userRepository.list(query);
        List<UserDTO> userDTOs = page.getResult()
            .stream()
            .map(userAssembler::toDTO)
            .collect(Collectors.toList());
        setRoles(userDTOs);
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), userDTOs);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindRole(UserBindRoleCommand userBindRoleCommand) {
        TiValidUtil.valid(userBindRoleCommand);
        Long userId = userBindRoleCommand.getUserId();
        List<Long> roleIds = Optional.ofNullable(userBindRoleCommand.getRoleIds()).orElseGet(ArrayList::new);
        userRoleRepository.removeAndSave(userId, roleIds);
    }

    public void lock(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        TiAssert.isTrue(!usernames.contains(SecurityConst.ADMIN), TiBizErrCode.FAIL, "管理员账户无法锁定");
        // 正常状态才能锁定
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.NORMAL.code());
        Integer count = userRepository.modifyStatus(usernames, UserStatus.LOCKED.code(), eqDbStatus, null);
        TiAssert.isTrue(count > 0, TiBizErrCode.FAIL, "无可锁定用户");
    }

    public void unLock(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        // 锁定状态才能解锁
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.LOCKED.code());
        Integer count = userRepository.modifyStatus(usernames, UserStatus.NORMAL.code(), eqDbStatus, null);
        TiAssert.isTrue(count > 0, TiBizErrCode.FAIL, "无可解锁用户");
    }

    public void logOut(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        TiAssert.isTrue(!usernames.contains(SecurityConst.ADMIN), TiBizErrCode.FAIL, "管理员账户无法注销");
        // 正常状态才能注销
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.NORMAL.code());
        Integer count = userRepository.modifyStatus(usernames, UserStatus.LOG_OUT.code(), eqDbStatus, null);
        TiAssert.isTrue(count > 0, TiBizErrCode.FAIL, "无可注销用户");
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        usernames.forEach(this::remove);
    }

    public void remove(String username) {
        TiAssert.isNotBlank(username, "用户名不能为空");
        User user = userRepository.getByUsername(username);
        TiAssert.isNotNull(user, TiBizErrCode.FAIL, "删除失败,用户不存在");
        TiAssert.isTrue(Objects.equals(UserStatus.LOG_OUT.code(), user.getStatus()), TiBizErrCode.FAIL, "删除失败,非注销用户");
        boolean removeUser = userRepository.removeByUsername(username);
        userRoleRepository.removeByUserId(user.getId());
        TiAssert.isNotNull(removeUser, TiBizErrCode.FAIL, "删除失败");
    }

    public void excelTemplateDownload() throws IOException {
        String sheetName = "用户信息";
        String fileName = "用户信息模板-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        ExcelHandle.writeEmptyToResponseBatch(fileName, sheetName, UserImpModel.class, response);
    }

    public void importExcel(MultipartFile file) throws IOException {
        String sheetName = "导入结果";
        String fileName = StrUtil.format("{}-导入结果", file.getOriginalFilename());
        Role guestRole = roleRepository.getGuestRole();
        TiAssert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        UserService bean = TiSpringUtil.getBean(this.getClass());
        Map<String, Integer> valueMap = dictExecutor.getValueMap(DictConst.SEX, NumberUtil::parseInt);
        ExcelHandle.readAndWriteToResponse((x, y) -> bean.readAndWrite(x, y, guestRole, valueMap), file, fileName, sheetName, UserImp.class, response);
    }

    @Transactional(rollbackFor = Exception.class)
    public void readAndWrite(List<UserImp> userImps, Consumer<UserImp> errHandle, Role guestRole, Map<String, Integer> valueMap) {
        List<User> users = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();
        for (UserImp userImp : userImps) {
            if (userImp.getIsError()) {
                continue;
            }
            String password = passwordEncoder.encode(CommConst.DEFAULT_PASSWORD);
            User user = userAssembler.toEntity(userImp, password, UserStatus.NORMAL.code(), valueMap.get(userImp.getSexName()));
            List<String> errorMsgs = checkRepeat(user);
            if (CollUtil.isNotEmpty(errorMsgs)) {
                userImp.setMessage(String.join(",", errorMsgs));
                errHandle.accept(userImp);
                continue;
            }
            userImp.setMessage("导入成功");
            users.add(user);
            UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleId(guestRole.getId())
                .build();
            userRoles.add(userRole);
        }
        if (CollUtil.isEmpty(users)) {
            return;
        }
        userRepository.saveBatch(users);
        userRoleRepository.saveBatch(userRoles);
    }


    public void exportExcel(UserQuery query) throws IOException {
        String sheetName = "用户信息";
        String fileName = "用户信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.USER_STATUS, DictConst.SEX);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, UserExp.class, response);
    }

    private Collection<UserExp> excelExpHandle(UserQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<User> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        userRepository.list(query);
        return page.getResult()
            .stream()
            .map(x -> {
                UserExp userExp = userAssembler.toExp(x);
                userExp.setStatusName(labelMap.get(DictConst.USER_STATUS + x.getStatus()));
                userExp.setSexName(labelMap.get(DictConst.SEX + x.getSex()));
                return userExp;
            })
            .collect(Collectors.toList());
    }

    public void modifyPassword(UserModifyPasswordCommand userModifyPasswordCommand) {
        TiValidUtil.valid(userModifyPasswordCommand);
        User dbUser = userRepository.getByUsername(userModifyPasswordCommand.getUsername());
        TiAssert.isNotEmpty(dbUser, TiBizErrCode.FAIL, "用户不存在");
        SecurityUser loginUser = UserUtil.getCurrentUser();
        // 非管理员用户，只能修改自己的用户
        if (!UserUtil.isAdmin(loginUser)) {
            TiAssert.isTrue(UserUtil.isSelf(dbUser, loginUser), TiBizErrCode.FAIL, "只能修改自己的密码");
        }
        modifyPassword(userModifyPasswordCommand.getPassword(), userModifyPasswordCommand.getNewPassword(), dbUser);
    }

    public void modifyPasswordForSelf(UserModifySelfPasswordCommand userModifySelfPasswordCommand) {
        TiValidUtil.valid(userModifySelfPasswordCommand);
        SecurityUser loginUser = UserUtil.getCurrentUser();
        User dbUser = userRepository.getByUsername(loginUser.getUsername());
        TiAssert.isNotEmpty(dbUser, TiBizErrCode.FAIL, "用户不存在");
        modifyPassword(userModifySelfPasswordCommand.getPassword(), userModifySelfPasswordCommand.getNewPassword(), dbUser);
    }

    private void modifyPassword(String password, String newPassword, User user) {
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        TiAssert.isTrue(matches, TiBizErrCode.FAIL, "密码错误");
        String encodedPasswordNew = passwordEncoder.encode(newPassword);
        user.modifyPassword(encodedPasswordNew);
        // 更新密码
        boolean modify = userRepository.modify(user);
        TiAssert.isTrue(modify, TiBizErrCode.FAIL, "更新密码失败");
    }

    /**
     * 保存或者修改用户信息重复数据判断，用户名称、邮箱、手机号保证其唯一性
     */
    private List<String> checkRepeat(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String mobile = user.getMobile();
        List<User> users = userRepository.getByAccount(username, email, mobile);
        boolean isModify = Objects.nonNull(user.getId());
        boolean usernameNotBlank = StrUtil.isNotBlank(username);
        boolean emailNotBlank = StrUtil.isNotBlank(email);
        boolean mobileNotBlank = StrUtil.isNotBlank(mobile);
        List<String> errors = new ArrayList<>();
        for (User item : users) {
            Long itemId = item.getId();
            if (isModify) {
                if (Objects.equals(user.getId(), itemId)) {
                    user = item;
                    continue;
                }
            }
            String itemUsername = item.getUsername();
            String itemMobile = item.getMobile();
            String itemEmail = item.getEmail();
            // 用户名重复判断
            if (usernameNotBlank && Objects.equals(username, itemUsername)) {
                errors.add("用户名已经存在");
            }
            // 邮箱重复判断
            if (emailNotBlank && Objects.equals(email, itemEmail)) {
                errors.add("邮箱已经存在");
            }
            // 手机号码重复判断
            if (mobileNotBlank && Objects.equals(mobile, itemMobile)) {
                errors.add("手机号已经存在");
            }
        }
        return errors;
    }

    public void setRoles(List<UserDTO> userDtos) {
        if (CollUtil.isEmpty(userDtos)) {
            return;
        }
        Map<Long, List<Long>> userRoleIdsMap = userDtos
            .stream()
            .collect(Collectors.toMap(UserDTO::getId, x -> userRoleRepository.listByUserId(x.getId())));
        List<Long> roleIds = userRoleIdsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        List<Role> roles = roleRepository.listByIds(roleIds);
        Map<Long, Role> roleMap = roles
            .stream()
            .collect(Collectors.toMap(Role::getId, Function.identity()));
        for (UserDTO userDto : userDtos) {
            Long id = userDto.getId();
            List<Long> itemRoleIds = Optional.ofNullable(userRoleIdsMap.get(id))
                .orElseGet(ArrayList::new);
            List<RoleDTO> roleDTOS = itemRoleIds
                .stream()
                .map(x -> roleAssembler.toDTO(roleMap.get(x)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            userDto.setRoleIds(itemRoleIds);
            userDto.setRoles(roleDTOS);
        }
    }

}
