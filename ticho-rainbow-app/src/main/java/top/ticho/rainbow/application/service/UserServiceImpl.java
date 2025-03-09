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
import top.ticho.rainbow.application.dto.PasswordDTO;
import top.ticho.rainbow.application.dto.RoleDTO;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.UserDTO;
import top.ticho.rainbow.application.dto.UserPasswordDTO;
import top.ticho.rainbow.application.dto.UserRoleDTO;
import top.ticho.rainbow.application.dto.command.FileUploadCommand;
import top.ticho.rainbow.application.dto.command.ResetPassworEmailSendCommand;
import top.ticho.rainbow.application.dto.command.ResetPasswordCommand;
import top.ticho.rainbow.application.dto.command.SignUpEmailSendCommand;
import top.ticho.rainbow.application.dto.excel.UserExp;
import top.ticho.rainbow.application.dto.excel.UserImp;
import top.ticho.rainbow.application.dto.excel.UserImpModel;
import top.ticho.rainbow.application.dto.query.UserAccountQuery;
import top.ticho.rainbow.application.dto.query.UserQuery;
import top.ticho.rainbow.application.dto.request.UserLoginDTO;
import top.ticho.rainbow.application.dto.response.FileInfoDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.entity.UserRole;
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
import top.ticho.starter.web.util.valid.TiValidGroup;
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
public class UserServiceImpl extends AbstractAuthServiceImpl implements UserService {
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

    @Override
    public void imgCode(String imgKey) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (OutputStream out = response.getOutputStream()) {
            TiAssert.isNotBlank(imgKey, "验证码秘钥不能为空");
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

    @Override
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

    @Override
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
        User user = new User();
        user.setUsername(username);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.NORMAL.code());
        UserAccountQuery accountDTO = userAssembler.toAccountQuery(user);
        preCheckRepeatUser(accountDTO, null);
        user.setNickname(username);
        TiAssert.isTrue(userRepository.save(user), TiBizErrCode.FAIL, "注册失败");
        Role guestRole = roleRepository.getGuestRole();
        TiAssert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        userRoleRepository.removeAndSave(user.getId(), Collections.singletonList(guestRole.getId()));
        // 返回登录使用参数
        return getUserLoginDTO(username);
    }

    @Override
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

    @Override
    public UserLoginDTO resetPassword(ResetPasswordCommand resetPasswordCommand) {
        String email = resetPasswordCommand.getEmail();
        String cacheEmailCode = springCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        TiAssert.isNotBlank(cacheEmailCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.RESET_PASSWORD_CODE, email);
        TiAssert.isTrue(resetPasswordCommand.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        User dbUser = userRepository.getByEmail(email);
        TiAssert.isNotNull(
            dbUser, TiBizErrCode.FAIL, () -> {
                log.info("重置用户{}密码失败，用户不存在", email);
                return "重置失败";
            }
        );
        String username = dbUser.getUsername();
        String encodedPasswordNew = passwordEncoder.encode(resetPasswordCommand.getPassword());
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(username);
        user.setPassword(encodedPasswordNew);
        // 更新密码
        boolean update = userRepository.modify(user);
        TiAssert.isTrue(update, TiBizErrCode.FAIL, "更新密码失败");
        // 返回登录使用参数
        return getUserLoginDTO(username);
    }

    @Override
    public void resetPassword(String username) {
        boolean admin = UserUtil.isAdmin();
        TiAssert.isTrue(admin, TiBizErrCode.FAIL, "无管理员操作权限");
        TiAssert.isTrue(!SecurityConst.ADMIN.equals(username), TiBizErrCode.FAIL, "管理员账户无法重置密码");
        UserDTO dbUser = getInfoByUsername(username);
        TiAssert.isNotNull(dbUser, "用户不存在");
        String encodedPasswordNew = passwordEncoder.encode(CommConst.DEFAULT_PASSWORD);
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(dbUser.getUsername());
        user.setPassword(encodedPasswordNew);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserDTO userDTO) {
        TiValidUtil.valid(userDTO, TiValidGroup.Add.class);
        String password = userDTO.getPassword();
        userDTO.setPassword(passwordEncoder.encode(password));
        User user = userAssembler.toEntity(userDTO);
        UserAccountQuery accountDTO = userAssembler.toAccountQuery(user);
        preCheckRepeatUser(accountDTO, null);
        TiAssert.isTrue(userRepository.save(user), TiBizErrCode.FAIL, "保存失败");
        if (CollUtil.isEmpty(userDTO.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), userDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(UserDTO userDTO) {
        TiValidUtil.valid(userDTO, TiValidGroup.Upd.class);
        userDTO.setPassword(null);
        User user = userAssembler.toEntity(userDTO);
        UserAccountQuery accountDTO = userAssembler.toAccountQuery(user);
        User dbUser = preCheckRepeatUser(accountDTO, userDTO.getId());
        // 用户名不能修改
        user.setUsername(dbUser.getUsername());
        TiAssert.isTrue(userRepository.modify(user), TiBizErrCode.FAIL, "修改失败");
        if (CollUtil.isEmpty(userDTO.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), userDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForSelf(UserDTO userDTO) {
        User dbUser = userRepository.getByUsername(UserUtil.getCurrentUsername());
        TiAssert.isNotNull(dbUser, TiBizErrCode.FAIL, "修改失败, 用户不存在");
        userDTO.setId(dbUser.getId());
        modify(userDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        User dbUser = userRepository.getByUsername(UserUtil.getCurrentUsername());
        TiAssert.isNotNull(dbUser, TiBizErrCode.FAIL, "头像上传失败, 用户不存在");
        FileUploadCommand fileUploadCommand = new FileUploadCommand();
        fileUploadCommand.setFile(file);
        fileUploadCommand.setType(1);
        fileUploadCommand.setRelativePath(StrUtil.format("user/{}/avatar", dbUser.getUsername()));
        fileUploadCommand.setRemark("头像");
        FileInfoDTO upload = fileInfoService.upload(fileUploadCommand);
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(dbUser.getUsername());
        user.setPhoto(upload.getId().toString());
        userRepository.modify(user);
        FileInfo fileInfo = fileInfoAssembler.toEntity(upload);
        return fileInfoService.getUrl(fileInfo, null, true);
    }

    @Override
    public UserDTO getInfoByUsername(String username) {
        User user = userRepository.getCacheByUsername(username);
        UserDTO userDTO = userAssembler.toDTO(user);
        Optional.ofNullable(userDTO).ifPresent(x -> setRoles(Collections.singletonList(x)));
        return userDTO;
    }

    @Override
    public UserDTO getInfo() {
        return getInfoByUsername(UserUtil.getCurrentUsername());
    }

    @Override
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRole(UserRoleDTO userRoleDTO) {
        TiValidUtil.valid(userRoleDTO);
        Long userId = userRoleDTO.getUserId();
        List<Long> roleIds = Optional.ofNullable(userRoleDTO.getRoleIds()).orElseGet(ArrayList::new);
        userRoleRepository.removeAndSave(userId, roleIds);
    }

    @Override
    public void lock(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        TiAssert.isTrue(!usernames.contains(SecurityConst.ADMIN), TiBizErrCode.FAIL, "管理员账户无法锁定");
        // 正常状态才能锁定
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.NORMAL.code());
        Integer count = userRepository.updateStatus(usernames, UserStatus.LOCKED.code(), eqDbStatus, null);
        TiAssert.isTrue(count > 0, TiBizErrCode.FAIL, "无可锁定用户");
    }

    @Override
    public void unLock(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        // 锁定状态才能解锁
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.LOCKED.code());
        Integer count = userRepository.updateStatus(usernames, UserStatus.NORMAL.code(), eqDbStatus, null);
        TiAssert.isTrue(count > 0, TiBizErrCode.FAIL, "无可解锁用户");
    }

    @Override
    public void logOut(List<String> usernames) {
        TiAssert.isNotEmpty(usernames, "用户名不能为空");
        TiAssert.isTrue(!usernames.contains(SecurityConst.ADMIN), TiBizErrCode.FAIL, "管理员账户无法注销");
        // 正常状态才能注销
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.NORMAL.code());
        Integer count = userRepository.updateStatus(usernames, UserStatus.LOG_OUT.code(), eqDbStatus, null);
        TiAssert.isTrue(count > 0, TiBizErrCode.FAIL, "无可注销用户");
    }

    @Override
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

    @Override
    public void impTemplate() throws IOException {
        String sheetName = "用户信息";
        String fileName = "用户信息模板-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        ExcelHandle.writeEmptyToResponseBatch(fileName, sheetName, UserImpModel.class, response);
    }

    @Override
    public void impExcel(MultipartFile file) throws IOException {
        String sheetName = "导入结果";
        String fileName = StrUtil.format("{}-导入结果", file.getOriginalFilename());
        Role guestRole = roleRepository.getGuestRole();
        TiAssert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        UserServiceImpl bean = TiSpringUtil.getBean(this.getClass());
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
            UserAccountQuery userQuery = new UserAccountQuery();
            userQuery.setUsername(userImp.getUsername());
            userQuery.setEmail(userImp.getEmail());
            userQuery.setMobile(userImp.getMobile());
            String errorMsg = checkImp(userQuery);
            if (Objects.nonNull(errorMsg)) {
                userImp.setMessage(errorMsg);
                errHandle.accept(userImp);
                continue;
            }
            userImp.setMessage("导入成功");
            User user = userAssembler.toEntity(userImp);
            user.setPassword(passwordEncoder.encode(CommConst.DEFAULT_PASSWORD));
            user.setStatus(UserStatus.NORMAL.code());
            user.setSex(valueMap.get(userImp.getSexName()));
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

    /**
     * 保存或者修改用户信息重复数据判断，用户名称、邮箱、手机号保证其唯一性
     *
     * @param userAccountQuery 用户登录账号信息
     */
    private String checkImp(UserAccountQuery userAccountQuery) {
        String username = userAccountQuery.getUsername();
        String email = userAccountQuery.getEmail();
        String mobile = userAccountQuery.getMobile();
        List<User> users = userRepository.getByAccount(userAccountQuery);
        boolean usernameNotBlank = StrUtil.isNotBlank(username);
        boolean emailNotBlank = StrUtil.isNotBlank(email);
        boolean mobileNotBlank = StrUtil.isNotBlank(mobile);
        for (User item : users) {
            String itemUsername = item.getUsername();
            String itemMobile = item.getMobile();
            String itemEmail = item.getEmail();
            // 用户名重复判断
            if (usernameNotBlank && Objects.equals(username, itemUsername)) {
                return "该用户名已经存在";
            }
            // 邮箱重复判断
            if (emailNotBlank && Objects.equals(email, itemEmail)) {
                return "该邮箱已经存在";
            }
            // 手机号码重复判断
            if (mobileNotBlank && Objects.equals(mobile, itemMobile)) {
                return "该手机号已经存在";
            }
        }
        return null;
    }

    @Override
    public void expExcel(UserQuery query) throws IOException {
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

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        TiValidUtil.valid(userPasswordDTO);
        User dbUser = userRepository.getByUsername(userPasswordDTO.getUsername());
        TiAssert.isNotEmpty(dbUser, TiBizErrCode.FAIL, "用户不存在");
        SecurityUser loginUser = UserUtil.getCurrentUser();
        // 非管理员用户，只能修改自己的用户
        if (!UserUtil.isAdmin(loginUser)) {
            TiAssert.isTrue(UserUtil.isSelf(dbUser, loginUser), TiBizErrCode.FAIL, "只能修改自己的密码");
        }
        updatePassword(userPasswordDTO.getPassword(), userPasswordDTO.getNewPassword(), dbUser);
    }

    @Override
    public void updatePasswordForSelf(PasswordDTO passwordDTO) {
        TiValidUtil.valid(passwordDTO);
        SecurityUser loginUser = UserUtil.getCurrentUser();
        User dbUser = userRepository.getByUsername(loginUser.getUsername());
        TiAssert.isNotEmpty(dbUser, TiBizErrCode.FAIL, "用户不存在");
        updatePassword(passwordDTO.getPassword(), passwordDTO.getNewPassword(), dbUser);
    }

    private void updatePassword(String password, String newPassword, User dbUser) {
        boolean matches = passwordEncoder.matches(password, dbUser.getPassword());
        TiAssert.isTrue(matches, TiBizErrCode.FAIL, "密码错误");
        String encodedPasswordNew = passwordEncoder.encode(newPassword);
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(dbUser.getUsername());
        user.setPassword(encodedPasswordNew);
        // 更新密码
        boolean update = userRepository.modify(user);
        TiAssert.isTrue(update, TiBizErrCode.FAIL, "更新密码失败");
    }

    /**
     * 保存或者修改用户信息重复数据判断，用户名称、邮箱、手机号保证其唯一性
     *
     * @param userAccountQuery 用户登录账号信息
     */
    private User preCheckRepeatUser(UserAccountQuery userAccountQuery, Long updateId) {
        String username = userAccountQuery.getUsername();
        String email = userAccountQuery.getEmail();
        String mobile = userAccountQuery.getMobile();
        List<User> users = userRepository.getByAccount(userAccountQuery);
        boolean isUpdate = Objects.nonNull(updateId);
        User user = null;
        boolean usernameNotBlank = StrUtil.isNotBlank(username);
        boolean emailNotBlank = StrUtil.isNotBlank(email);
        boolean mobileNotBlank = StrUtil.isNotBlank(mobile);
        for (User item : users) {
            Long itemId = item.getId();
            if (isUpdate) {
                if (Objects.equals(updateId, itemId)) {
                    user = item;
                    continue;
                }
            }
            String itemUsername = item.getUsername();
            String itemMobile = item.getMobile();
            String itemEmail = item.getEmail();
            // 用户名重复判断
            if (usernameNotBlank) {
                TiAssert.isTrue(!Objects.equals(username, itemUsername), TiBizErrCode.FAIL, "该用户名已经存在");
            }
            // 邮箱重复判断
            if (emailNotBlank) {
                TiAssert.isTrue(!Objects.equals(email, itemEmail), TiBizErrCode.FAIL, "该邮箱已经存在");
            }
            // 手机号码重复判断
            if (mobileNotBlank) {
                TiAssert.isTrue(!Objects.equals(mobile, itemMobile), TiBizErrCode.FAIL, "该手机号已经存在");
            }
        }
        return user;
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
