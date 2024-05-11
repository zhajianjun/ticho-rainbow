package top.ticho.rainbow.domain.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.boot.json.util.JsonUtil;
import top.ticho.boot.mail.component.MailContent;
import top.ticho.boot.mail.component.MailInines;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.file.BaseMultPartFile;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.SpringContext;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.rainbow.domain.handle.AuthHandle;
import top.ticho.rainbow.domain.handle.DictTemplate;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.component.cache.SpringCacheTemplate;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.constant.CommConst;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.core.enums.UserStatus;
import top.ticho.rainbow.infrastructure.core.util.BeetlUtil;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.infrastructure.entity.Role;
import top.ticho.rainbow.infrastructure.entity.User;
import top.ticho.rainbow.infrastructure.entity.UserRole;
import top.ticho.rainbow.interfaces.assembler.FileInfoAssembler;
import top.ticho.rainbow.interfaces.assembler.RoleAssembler;
import top.ticho.rainbow.interfaces.assembler.UserAssembler;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.dto.ImgCodeDTO;
import top.ticho.rainbow.interfaces.dto.ImgCodeEmailDTO;
import top.ticho.rainbow.interfaces.dto.PasswordDTO;
import top.ticho.rainbow.interfaces.dto.RoleDTO;
import top.ticho.rainbow.interfaces.dto.SecurityUser;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserPasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpOrResetDTO;
import top.ticho.rainbow.interfaces.excel.UserExp;
import top.ticho.rainbow.interfaces.excel.UserImp;
import top.ticho.rainbow.interfaces.excel.UserImpModel;
import top.ticho.rainbow.interfaces.query.UserAccountQuery;
import top.ticho.rainbow.interfaces.query.UserQuery;

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
@Service
@Slf4j
public class UserServiceImpl extends AuthHandle implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired(required = false)
    private HttpServletResponse response;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SpringCacheTemplate springCacheTemplate;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private DictTemplate dictTemplate;

    @Override
    public void imgCode(String imgKey) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (OutputStream out = response.getOutputStream()) {
            Assert.isNotBlank(imgKey, "验证码秘钥不能为空");
            LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
            gifCaptcha.createCode();
            String code = gifCaptcha.getCode();
            springCacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
            BufferedImage buffImg = gifCaptcha.getImage();
            ImageIO.write(buffImg, "png", out);
        } catch (Exception e) {
            log.error("获取验证码失败，error {}", e.getMessage(), e);
            String message = e.getMessage();
            int code = BizErrCode.FAIL.getCode();
            if (e instanceof BizException) {
                BizException bizException = ((BizException) e);
                code = bizException.getCode();
                message = bizException.getMsg();
            }
            Result<String> result = Result.of(code, message, null);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(JsonUtil.toJsonString(result));
        }
    }

    public void imgCodeValid(ImgCodeDTO imgCodeDTO) {
        ValidUtil.valid(imgCodeDTO, ImgCodeDTO.ImgCodeValid.class);
        String imgCode = imgCodeDTO.getImgCode();
        String key = imgCodeDTO.getImgKey();
        String cacheImgCode = springCacheTemplate.get(CacheConst.VERIFY_CODE, key, String.class);
        Assert.isNotBlank(cacheImgCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.VERIFY_CODE, key);
        Assert.isTrue(imgCode.equalsIgnoreCase(cacheImgCode), "验证码不正确");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signUpEmailSend(ImgCodeEmailDTO imgCodeEmailDTO) {
        // 图片验证码校验
        ValidUtil.valid(imgCodeEmailDTO);
        imgCodeValid(imgCodeEmailDTO);
        // 发送邮箱验证码
        String email = imgCodeEmailDTO.getEmail();
        User dbUser = userRepository.getByEmail(email);
        Assert.isNull(dbUser, "用户已存在");
        String code = springCacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        Assert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        springCacheTemplate.put(CacheConst.SIGN_UP_CODE, email, code);
        GroupTemplate groupTemplate = BeetlUtil.getGroupTemplate(true);
        Template template = groupTemplate.getTemplate("/template/signUpEmailSend.html");
        MailInines mailInines = new MailInines();
        mailInines.setContentId("p01");
        mailInines.setFile(new BaseMultPartFile("captcha", "captcha.png", MediaType.IMAGE_PNG_VALUE, gifCaptcha.getImageBytes()));
        MailContent mailContent = new MailContent();
        mailContent.setTo(email);
        mailContent.setSubject("注册");
        mailContent.setContent(template.render());
        mailContent.setInlines(Collections.singletonList(mailInines));
        boolean sendMail = emailRepository.sendMail(mailContent);
        Assert.isTrue(sendMail, "发送邮件失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginDTO signUp(UserSignUpOrResetDTO userSignUpOrResetDTO) {
        ValidUtil.valid(userSignUpOrResetDTO);
        String email = userSignUpOrResetDTO.getEmail();
        String cacheEmailCode = springCacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        Assert.isNotBlank(cacheEmailCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.SIGN_UP_CODE, email);
        Assert.isTrue(userSignUpOrResetDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        String username = userSignUpOrResetDTO.getUsername();
        String password = userSignUpOrResetDTO.getPassword();
        User user = new User();
        user.setId(CloudIdUtil.getId());
        user.setUsername(username);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.NORMAL.code());
        UserAccountQuery accountDTO = UserAssembler.INSTANCE.entityToAccount(user);
        preCheckRepeatUser(accountDTO, null);
        user.setNickname(username);
        Assert.isTrue(userRepository.save(user), BizErrCode.FAIL, "注册失败");
        Role guestRole = roleRepository.getGuestRole();
        Assert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        userRoleRepository.removeAndSave(user.getId(), Collections.singletonList(guestRole.getId()));
        // 返回登录使用参数
        return getUserLoginDTO(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetPasswordEmailSend(ImgCodeEmailDTO imgCodeEmailDTO) {
        ValidUtil.valid(imgCodeEmailDTO);
        imgCodeValid(imgCodeEmailDTO);
        // 发送邮箱验证码
        String email = imgCodeEmailDTO.getEmail();
        User dbUser = userRepository.getByEmail(email);
        Assert.isNotNull(dbUser, "用户不存在");
        String code = springCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        Assert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        springCacheTemplate.put(CacheConst.RESET_PASSWORD_CODE, email, code);
        GroupTemplate groupTemplate = BeetlUtil.getGroupTemplate(true);
        Template template = groupTemplate.getTemplate("/template/resetPasswordEmailSend.html");
        MailInines mailInines = new MailInines();
        mailInines.setContentId("p01");
        mailInines.setFile(new BaseMultPartFile("captcha", "captcha.png", MediaType.IMAGE_PNG_VALUE, gifCaptcha.getImageBytes()));
        MailContent mailContent = new MailContent();
        mailContent.setTo(email);
        mailContent.setSubject("重置密码");
        mailContent.setContent(template.render());
        mailContent.setInlines(Collections.singletonList(mailInines));
        boolean sendMail = emailRepository.sendMail(mailContent);
        Assert.isTrue(sendMail, "发送邮件失败");
        return dbUser.getUsername();
    }

    @Override
    public UserLoginDTO resetPassword(UserSignUpOrResetDTO userSignUpOrResetDTO) {
        ValidUtil.valid(userSignUpOrResetDTO);
        String email = userSignUpOrResetDTO.getEmail();
        String cacheEmailCode = springCacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        Assert.isNotBlank(cacheEmailCode, "验证码已过期");
        springCacheTemplate.evict(CacheConst.RESET_PASSWORD_CODE, email);
        Assert.isTrue(userSignUpOrResetDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        User dbUser = userRepository.getByEmail(email);
        Assert.isNotNull(dbUser, BizErrCode.FAIL, () -> {
            log.info("重置用户{}密码失败，用户不存在", email);
            return "重置失败";
        });
        String username = dbUser.getUsername();
        String encodedPasswordNew = passwordEncoder.encode(userSignUpOrResetDTO.getPassword());
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(username);
        user.setPassword(encodedPasswordNew);
        // 更新密码
        boolean update = userRepository.updateById(user);
        Assert.isTrue(update, BizErrCode.FAIL, "更新密码失败");
        // 返回登录使用参数
        return getUserLoginDTO(username);
    }

    @Override
    public void resetPassword(String username) {
        boolean admin = UserUtil.isAdmin();
        Assert.isTrue(admin, BizErrCode.FAIL, "无管理员操作权限");
        Assert.isTrue(!SecurityConst.ADMIN.equals(username), BizErrCode.FAIL, "管理员账户无法重置密码");
        UserDTO dbUser = getInfoByUsername(username);
        Assert.isNotNull(dbUser, "用户不存在");
        String encodedPasswordNew = passwordEncoder.encode(CommConst.DEFAULT_PASSWORD);
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(dbUser.getUsername());
        user.setPassword(encodedPasswordNew);
        Assert.isTrue(userRepository.updateById(user), BizErrCode.FAIL, "重置密码失败");
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
        ValidUtil.valid(userDTO, ValidGroup.Add.class);
        String password = userDTO.getPassword();
        userDTO.setPassword(passwordEncoder.encode(password));
        User user = UserAssembler.INSTANCE.dtoToEntity(userDTO);
        UserAccountQuery accountDTO = UserAssembler.INSTANCE.entityToAccount(user);
        preCheckRepeatUser(accountDTO, null);
        Assert.isTrue(userRepository.save(user), BizErrCode.FAIL, "保存失败");
        if (CollUtil.isEmpty(userDTO.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), userDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDTO userDTO) {
        ValidUtil.valid(userDTO, ValidGroup.Upd.class);
        userDTO.setPassword(null);
        User user = UserAssembler.INSTANCE.dtoToEntity(userDTO);
        UserAccountQuery accountDTO = UserAssembler.INSTANCE.entityToAccount(user);
        User dbUser = preCheckRepeatUser(accountDTO, userDTO.getId());
        // 用户名不能修改
        user.setUsername(dbUser.getUsername());
        Assert.isTrue(userRepository.updateById(user), BizErrCode.FAIL, "修改失败");
        if (CollUtil.isEmpty(userDTO.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), userDTO.getRoleIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForSelf(UserDTO userDTO) {
        User dbUser = userRepository.getByUsername(UserUtil.getCurrentUsername());
        Assert.isNotNull(dbUser, BizErrCode.FAIL, "修改失败, 用户不存在");
        userDTO.setId(dbUser.getId());
        update(userDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        User dbUser = userRepository.getByUsername(UserUtil.getCurrentUsername());
        Assert.isNotNull(dbUser, BizErrCode.FAIL, "头像上传失败, 用户不存在");
        FileInfoReqDTO fileInfoReqDTO = new FileInfoReqDTO();
        fileInfoReqDTO.setFile(file);
        fileInfoReqDTO.setType(1);
        fileInfoReqDTO.setRelativePath(StrUtil.format("user/{}/avatar", dbUser.getUsername()));
        fileInfoReqDTO.setRemark("头像");
        FileInfoDTO upload = fileInfoService.upload(fileInfoReqDTO);
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(dbUser.getUsername());
        user.setPhoto(upload.getId().toString());
        userRepository.updateById(user);
        FileInfo fileInfo = FileInfoAssembler.INSTANCE.dtoToEntity(upload);
        return fileInfoService.getUrl(fileInfo, null, true);
    }

    @Override
    public UserDTO getInfoByUsername(String username) {
        User user = userRepository.getCacheByUsername(username);
        UserDTO userDTO = UserAssembler.INSTANCE.entityToDto(user);
        Optional.ofNullable(userDTO).ifPresent(x -> setRoles(Collections.singletonList(x)));
        return userDTO;
    }

    @Override
    public UserDTO getInfo() {
        return getInfoByUsername(UserUtil.getCurrentUsername());
    }

    @Override
    public PageResult<UserDTO> page(UserQuery query) {
        // @formatter:off
        query.checkPage();
        Page<User> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        userRepository.list(query);
        List<UserDTO> userDTOs = page.getResult()
            .stream()
            .map(UserAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        setRoles(userDTOs);
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), userDTOs);
        // @formatter:on
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRole(UserRoleDTO userRoleDTO) {
        // @formatter:off
        ValidUtil.valid(userRoleDTO);
        Long userId = userRoleDTO.getUserId();
        List<Long> roleIds = Optional.ofNullable(userRoleDTO.getRoleIds()).orElseGet(ArrayList::new);
        userRoleRepository.removeAndSave(userId, roleIds);
        // @formatter:on
    }

    @Override
    public void lock(List<String> usernames) {
        Assert.isNotEmpty(usernames, "用户名不能为空");
        Assert.isTrue(!usernames.contains(SecurityConst.ADMIN), BizErrCode.FAIL, "管理员账户无法锁定");
        // 正常状态才能锁定
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.NORMAL.code());
        Integer count = userRepository.updateStatus(usernames, UserStatus.LOCKED.code(), eqDbStatus, null);
        Assert.isTrue(count > 0, BizErrCode.FAIL, "无可锁定用户");
    }

    @Override
    public void unLock(List<String> usernames) {
        Assert.isNotEmpty(usernames, "用户名不能为空");
        // 锁定状态才能解锁
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.LOCKED.code());
        Integer count = userRepository.updateStatus(usernames, UserStatus.NORMAL.code(), eqDbStatus, null);
        Assert.isTrue(count > 0, BizErrCode.FAIL, "无可解锁用户");
    }

    @Override
    public void logOut(List<String> usernames) {
        Assert.isNotEmpty(usernames, "用户名不能为空");
        Assert.isTrue(!usernames.contains(SecurityConst.ADMIN), BizErrCode.FAIL, "管理员账户无法注销");
        // 正常状态才能注销
        List<Integer> eqDbStatus = Collections.singletonList(UserStatus.NORMAL.code());
        Integer count = userRepository.updateStatus(usernames, UserStatus.LOG_OUT.code(), eqDbStatus, null);
        Assert.isTrue(count > 0, BizErrCode.FAIL, "无可注销用户");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> usernames) {
        Assert.isNotEmpty(usernames, "用户名不能为空");
        usernames.forEach(this::remove);
    }

    public void remove(String username) {
        Assert.isNotBlank(username, "用户名不能为空");
        User user = userRepository.getByUsername(username);
        Assert.isNotNull(user, BizErrCode.FAIL, "删除失败,用户不存在");
        Assert.isTrue(Objects.equals(UserStatus.LOG_OUT.code(), user.getStatus()), BizErrCode.FAIL, "删除失败,非注销用户");
        boolean removeUser = userRepository.removeByUsername(username);
        userRoleRepository.removeByUserId(user.getId());
        Assert.isNotNull(removeUser, BizErrCode.FAIL, "删除失败");
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
        Assert.isNotNull(guestRole, "默认角色不存在，请联系管理员进行处理");
        UserServiceImpl bean = SpringContext.getBean(this.getClass());
        Map<String, Integer> valueMap = dictTemplate.getValueMap(DictConst.SEX, NumberUtil::parseInt);
        ExcelHandle.readAndWriteToResponse((x, y)-> bean.readAndWrite(x, y, guestRole, valueMap), file, fileName, sheetName, UserImp.class, response);
    }

    @Transactional(rollbackFor = Exception.class)
    public void readAndWrite(List<UserImp> userImps, Consumer<UserImp> errHandle, Role guestRole, Map<String, Integer> valueMap) {
        List<User> users = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();
        for (UserImp userImp : userImps) {
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
            User user = UserAssembler.INSTANCE.impToEntity(userImp);
            user.setId(CloudIdUtil.getId());
            user.setPassword(passwordEncoder.encode(CommConst.DEFAULT_PASSWORD));
            user.setStatus(UserStatus.NORMAL.code());
            user.setSex(valueMap.get(userImp.getSexName()));
            users.add(user);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(guestRole.getId());
            userRoles.add(userRole);
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
        String fileName = "用户信息-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictTemplate.getLabelMapBatch(DictConst.USER_STATUS, DictConst.SEX);
        ExcelHandle.writeToResponseBatch(x-> this.excelExpHandle(x, labelMap), query, fileName, sheetName, UserExp.class, response);
    }

    private Collection<UserExp> excelExpHandle(UserQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<User> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        userRepository.list(query);
        return page.getResult()
            .stream()
            .map(x-> {
                UserExp userExp = UserAssembler.INSTANCE.entityToExp(x);
                userExp.setStatusName(labelMap.get(DictConst.USER_STATUS + x.getStatus()));
                userExp.setSexName(labelMap.get(DictConst.SEX + x.getSex()));
                return userExp;
            })
            .collect(Collectors.toList());
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        ValidUtil.valid(userPasswordDTO);
        User dbUser = userRepository.getByUsername(userPasswordDTO.getUsername());
        Assert.isNotEmpty(dbUser, BizErrCode.FAIL, "用户不存在");
        SecurityUser loginUser = UserUtil.getCurrentUser();
        // 非管理员用户，只能修改自己的用户
        if (!UserUtil.isAdmin(loginUser)) {
            Assert.isTrue(UserUtil.isSelf(dbUser, loginUser), BizErrCode.FAIL, "只能修改自己的密码");
        }
        updatePassword(userPasswordDTO, dbUser);
    }

    @Override
    public void updatePasswordForSelf(PasswordDTO passwordDTO) {
        ValidUtil.valid(passwordDTO);
        SecurityUser loginUser = UserUtil.getCurrentUser();
        User dbUser = userRepository.getByUsername(loginUser.getUsername());
        Assert.isNotEmpty(dbUser, BizErrCode.FAIL, "用户不存在");
        updatePassword(passwordDTO, dbUser);
    }

    private void updatePassword(PasswordDTO passwordDTO, User dbUser) {
        boolean matches = passwordEncoder.matches(passwordDTO.getPassword(), dbUser.getPassword());
        Assert.isTrue(matches, BizErrCode.FAIL, "密码错误");
        String encodedPasswordNew = passwordEncoder.encode(passwordDTO.getNewPassword());
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(dbUser.getUsername());
        user.setPassword(encodedPasswordNew);
        // 更新密码
        boolean update = userRepository.updateById(user);
        Assert.isTrue(update, BizErrCode.FAIL, "更新密码失败");
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
                Assert.isTrue(!Objects.equals(username, itemUsername), BizErrCode.FAIL, "该用户名已经存在");
            }
            // 邮箱重复判断
            if (emailNotBlank) {
                Assert.isTrue(!Objects.equals(email, itemEmail), BizErrCode.FAIL, "该邮箱已经存在");
            }
            // 手机号码重复判断
            if (mobileNotBlank) {
                Assert.isTrue(!Objects.equals(mobile, itemMobile), BizErrCode.FAIL, "该手机号已经存在");
            }
        }
        return user;
    }

    public void setRoles(List<UserDTO> userDtos) {
        // @formatter:off
        if (CollUtil.isEmpty(userDtos)) {
            return;
        }
        Map<Long, List<Long>> userRoleIdsMap = userDtos
            .stream()
            .collect(Collectors.toMap(UserDTO::getId, x-> userRoleRepository.listByUserId(x.getId())));
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
                .map(x-> RoleAssembler.INSTANCE.entityToDto(roleMap.get(x)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            userDto.setRoleIds(itemRoleIds);
            userDto.setRoles(roleDTOS);
        }
        // @formatter:on
    }

}
