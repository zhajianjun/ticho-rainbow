package top.ticho.rainbow.domain.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
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
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.rainbow.domain.handle.AuthHandle;
import top.ticho.rainbow.domain.repository.EmailRepository;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.component.CacheTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.enums.UserStatus;
import top.ticho.rainbow.infrastructure.core.util.BeetlUtil;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.rainbow.infrastructure.entity.Role;
import top.ticho.rainbow.infrastructure.entity.User;
import top.ticho.rainbow.interfaces.assembler.RoleAssembler;
import top.ticho.rainbow.interfaces.assembler.UserAssembler;
import top.ticho.rainbow.interfaces.dto.ImgCodeDTO;
import top.ticho.rainbow.interfaces.dto.ImgCodeEmailDTO;
import top.ticho.rainbow.interfaces.dto.RoleDTO;
import top.ticho.rainbow.interfaces.dto.SecurityUser;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserPasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpOrResetDTO;
import top.ticho.rainbow.interfaces.query.UserAccountQuery;
import top.ticho.rainbow.interfaces.query.UserQuery;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private CacheTemplate cacheTemplate;

    @Autowired
    private EmailRepository emailRepository;

    public void imgCodeValid(ImgCodeDTO imgCodeDTO) {
        ValidUtil.valid(imgCodeDTO, ImgCodeDTO.ImgCodeValid.class);
        String imgCode = imgCodeDTO.getImgCode();
        String key = imgCodeDTO.getImgKey();
        String cacheImgCode = cacheTemplate.get(CacheConst.VERIFY_CODE, key, String.class);
        Assert.isNotBlank(cacheImgCode, "验证码已过期");
        cacheTemplate.evict(CacheConst.VERIFY_CODE, key);
        Assert.isTrue(imgCode.equalsIgnoreCase(cacheImgCode), "验证码不正确");
    }

    @Override
    public void signUpEmailSend(ImgCodeEmailDTO imgCodeEmailDTO) {
        // 图片验证码校验
        ValidUtil.valid(imgCodeEmailDTO);
        imgCodeValid(imgCodeEmailDTO);
        // 发送邮箱验证码
        String email = imgCodeEmailDTO.getEmail();
        User dbUser = userRepository.getByEmail(email);
        Assert.isNull(dbUser, "用户已存在");
        String code = cacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        Assert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        cacheTemplate.put(CacheConst.SIGN_UP_CODE, email, code);
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
        emailRepository.sendMail(mailContent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginDTO signUp(UserSignUpOrResetDTO userSignUpOrResetDTO) {
        ValidUtil.valid(userSignUpOrResetDTO);
        String email = userSignUpOrResetDTO.getEmail();
        String cacheEmailCode = cacheTemplate.get(CacheConst.SIGN_UP_CODE, email, String.class);
        Assert.isNotBlank(cacheEmailCode, "验证码已过期");
        cacheTemplate.evict(CacheConst.SIGN_UP_CODE, email);
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
    public String resetPasswordEmailSend(ImgCodeEmailDTO imgCodeEmailDTO) {
        ValidUtil.valid(imgCodeEmailDTO);
        imgCodeValid(imgCodeEmailDTO);
        // 发送邮箱验证码
        String email = imgCodeEmailDTO.getEmail();
        User dbUser = userRepository.getByEmail(email);
        Assert.isNotNull(dbUser, "用户不存在");
        String code = cacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        Assert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        cacheTemplate.put(CacheConst.RESET_PASSWORD_CODE, email, code);
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
        emailRepository.sendMail(mailContent);
        return dbUser.getUsername();
    }

    @Override
    public UserLoginDTO resetPassword(UserSignUpOrResetDTO userSignUpOrResetDTO) {
        ValidUtil.valid(userSignUpOrResetDTO);
        String email = userSignUpOrResetDTO.getEmail();
        String cacheEmailCode = cacheTemplate.get(CacheConst.RESET_PASSWORD_CODE, email, String.class);
        Assert.isNotBlank(cacheEmailCode, "验证码已过期");
        cacheTemplate.evict(CacheConst.RESET_PASSWORD_CODE, email);
        Assert.isTrue(userSignUpOrResetDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        User dbUser = userRepository.getByEmail(email);
        Assert.isNotNull(dbUser, BizErrCode.FAIL, () -> {
            log.info("重置用户" + email + "密码失败，用户不存在");
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
        UserDTO dbUser = getByUsername(username);
        Assert.isNotNull(dbUser, "用户不存在");
        String encodedPasswordNew = passwordEncoder.encode("123456");
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
        cacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
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
    public void removeByUsername(String username) {
        Assert.isNotBlank(username, "用户名不能为空");
        User user = userRepository.getByUsername(username);
        Assert.isNotNull(user, BizErrCode.FAIL, "注销失败,用户不存在");
        // 账户注销
        user.setStatus(UserStatus.LOG_OUT.code());
        user.setUsername(username);
        boolean b = userRepository.updateById(user);
        Assert.isNotNull(b, BizErrCode.FAIL, "注销失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(UserDTO userDTO) {
        ValidUtil.valid(userDTO, ValidGroup.Upd.class);
        userDTO.setPassword(null);
        User user = UserAssembler.INSTANCE.dtoToEntity(userDTO);
        UserAccountQuery accountDTO = UserAssembler.INSTANCE.entityToAccount(user);
        preCheckRepeatUser(accountDTO, userDTO.getId());
        Assert.isTrue(userRepository.updateById(user), BizErrCode.FAIL, "修改失败");
        if (CollUtil.isEmpty(userDTO.getRoleIds())) {
            return;
        }
        userRoleRepository.removeAndSave(user.getId(), userDTO.getRoleIds());
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        UserDTO userDTO = UserAssembler.INSTANCE.entityToDto(user);
        Optional.ofNullable(userDTO).ifPresent(x-> setRoles(Collections.singletonList(x)));
        return userDTO;
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
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        ValidUtil.valid(userPasswordDTO);
        String username = userPasswordDTO.getUsername();
        String password = userPasswordDTO.getPassword();
        String passwordNew = userPasswordDTO.getNewPassword();
        User dbUser = userRepository.getByUsername(username);
        Assert.isNotEmpty(dbUser, BizErrCode.FAIL, "用户不存在");
        String encodedPassword = dbUser.getPassword();
        SecurityUser loginUser = UserUtil.getCurrentUser();
        // 非管理员用户，只能修改自己的用户
        if (!UserUtil.isAdmin(loginUser)) {
            Assert.isTrue(UserUtil.isSelf(dbUser, loginUser), BizErrCode.FAIL, "只能修改自己的密码");
        }
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        Assert.isTrue(matches, BizErrCode.FAIL, "密码错误");
        String encodedPasswordNew = passwordEncoder.encode(passwordNew);
        User user = new User();
        user.setId(dbUser.getId());
        user.setUsername(username);
        user.setPassword(encodedPasswordNew);
        // 更新密码
        boolean update = userRepository.updateById(user);
        Assert.isTrue(update, BizErrCode.FAIL, "更新密码失败");
    }

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
            cacheTemplate.put(CacheConst.VERIFY_CODE, imgKey, code);
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

    /**
     * 保存或者修改用户信息重复数据判断，用户名称、邮箱、手机号保证其唯一性
     *
     * @param userAccountQuery 用户登录账号信息
     */
    private void preCheckRepeatUser(UserAccountQuery userAccountQuery, Long updateId) {
        String username = userAccountQuery.getUsername();
        String email = userAccountQuery.getEmail();
        String mobile = userAccountQuery.getMobile();
        List<User> users = userRepository.getByAccount(userAccountQuery);
        boolean isUpdate = Objects.nonNull(updateId);
        for (User item : users) {
            Long itemId = item.getId();
            if (isUpdate && Objects.equals(updateId, itemId)) {
                continue;
            }
            String itemUsername = item.getUsername();
            String itemMobile = item.getMobile();
            String itemEmail = item.getEmail();
            // 用户名重复判断
            Assert.isTrue(!Objects.equals(username, itemUsername), BizErrCode.FAIL, "该用户名已经存在");
            // 手机号码重复判断
            Assert.isTrue(!Objects.equals(mobile, itemMobile), BizErrCode.FAIL, "该手机号已经存在");
            // 邮箱重复判断
            Assert.isTrue(!Objects.equals(email, itemEmail), BizErrCode.FAIL, "该邮箱已经存在");
        }
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
