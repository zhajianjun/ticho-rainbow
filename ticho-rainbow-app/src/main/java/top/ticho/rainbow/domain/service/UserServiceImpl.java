package top.ticho.rainbow.domain.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.RegexPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
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
import top.ticho.rainbow.interfaces.dto.RoleDTO;
import top.ticho.rainbow.interfaces.dto.SecurityUser;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.rainbow.interfaces.dto.UserPasswordDTO;
import top.ticho.rainbow.interfaces.dto.UserRoleDTO;
import top.ticho.rainbow.interfaces.dto.UserSignUpDTO;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginDTO signUp(UserSignUpDTO userSignUpDTO) {
        ValidUtil.valid(userSignUpDTO);
        String email = userSignUpDTO.getEmail();
        String cacheEmailCode = cacheTemplate.get(CacheConst.VERIFY_CODE, email, String.class);
        Assert.isNotBlank(cacheEmailCode, "验证码过期或者不存在");
        cacheTemplate.evict(CacheConst.VERIFY_CODE, email);
        Assert.isTrue(userSignUpDTO.getEmailCode().equalsIgnoreCase(cacheEmailCode), "验证码不正确");
        String username = userSignUpDTO.getUsername();
        String password = userSignUpDTO.getPassword();
        User user = new User();
        user.setId(CloudIdUtil.getId());
        user.setUsername(username);
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
    public void signUpEmailSend(String email) {
        Assert.isNotBlank(email, "邮箱不能为空");
        boolean match = ReUtil.isMatch(RegexPool.EMAIL, email);
        Assert.isTrue(match, "邮箱格式不正确");
        String code = cacheTemplate.get(CacheConst.VERIFY_CODE, email, String.class);
        Assert.isBlank(code, "验证码已发送，请稍后再试");
        LineCaptcha gifCaptcha = CaptchaUtil.createLineCaptcha(160, 40, 4, 150);
        gifCaptcha.createCode();
        code = gifCaptcha.getCode();
        cacheTemplate.put(CacheConst.VERIFY_CODE, email, code);
        GroupTemplate groupTemplate = BeetlUtil.getGroupTemplate(true);
        Template template = groupTemplate.getTemplate("/template/mail.html");
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
    public void confirm(String username) {
        Assert.isNotBlank(username, BizErrCode.PARAM_ERROR, "账户不能为空");
        User user = userRepository.getByUsername(username);
        User updateEntity = new User();
        updateEntity.setId(user.getId());
        updateEntity.setStatus(UserStatus.NORMAL.code());
        updateEntity.setUsername(username);
        Assert.isTrue(userRepository.updateById(updateEntity), BizErrCode.FAIL, "确认失败");
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
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(user.getId());
        userRoleDTO.setRoleIds(userDTO.getRoleIds());
        bindRole(userRoleDTO);
    }

    @Override
    public void removeByUsername(String username) {
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
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(user.getId());
        userRoleDTO.setRoleIds(userDTO.getRoleIds());
        bindRole(userRoleDTO);
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        UserDTO userDTO = UserAssembler.INSTANCE.entityToDto(user);
        setRoles(Collections.singletonList(userDTO));
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
        User queryUser = userRepository.getByUsername(username);
        Assert.isNotEmpty(queryUser, BizErrCode.FAIL, "用户不存在");
        String encodedPassword = queryUser.getPassword();
        SecurityUser loginUser = UserUtil.getCurrentUser();
        // 非管理员用户，只能修改自己的用户
        if (!UserUtil.isAdmin(loginUser)) {
            Assert.isTrue(UserUtil.isSelf(queryUser, loginUser), BizErrCode.FAIL, "只能修改自己的密码");
        }
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        Assert.isTrue(matches, BizErrCode.FAIL, "密码错误");
        String encodedPasswordNew = passwordEncoder.encode(passwordNew);
        User user = new User();
        user.setId(queryUser.getId());
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
