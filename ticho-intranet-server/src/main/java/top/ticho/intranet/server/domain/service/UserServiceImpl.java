package top.ticho.intranet.server.domain.service;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.UserService;
import top.ticho.intranet.server.domain.handle.UpmsHandle;
import top.ticho.intranet.server.domain.repository.RoleRepository;
import top.ticho.intranet.server.domain.repository.UserRepository;
import top.ticho.intranet.server.domain.repository.UserRoleRepository;
import top.ticho.intranet.server.infrastructure.core.enums.UserStatus;
import top.ticho.intranet.server.infrastructure.core.util.CaptchaUtil;
import top.ticho.intranet.server.infrastructure.core.util.UserUtil;
import top.ticho.intranet.server.infrastructure.entity.Role;
import top.ticho.intranet.server.infrastructure.entity.User;
import top.ticho.intranet.server.interfaces.assembler.RoleAssembler;
import top.ticho.intranet.server.interfaces.assembler.UserAssembler;
import top.ticho.intranet.server.interfaces.dto.RoleDTO;
import top.ticho.intranet.server.interfaces.dto.SecurityUser;
import top.ticho.intranet.server.interfaces.dto.UserDTO;
import top.ticho.intranet.server.interfaces.dto.UserPasswordDTO;
import top.ticho.intranet.server.interfaces.dto.UserRoleDTO;
import top.ticho.intranet.server.interfaces.dto.UserSignUpDTO;
import top.ticho.intranet.server.interfaces.query.UserAccountQuery;
import top.ticho.intranet.server.interfaces.query.UserQuery;
import top.ticho.trace.spring.util.IpUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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
public class UserServiceImpl extends UpmsHandle implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired(required = false)
    private HttpServletResponse response;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void signUp(UserSignUpDTO userSignUpDTO) {
        ValidUtil.valid(userSignUpDTO);
        String username = userSignUpDTO.getUsername();
        String password = userSignUpDTO.getPassword();
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.NOT_ACTIVE.code());
        UserAccountQuery accountDTO = UserAssembler.INSTANCE.entityToAccount(user);
        preCheckRepeatUser(accountDTO, null);
        Assert.isTrue(userRepository.save(user), BizErrCode.FAIL, "注册失败");
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
        // @formatter:off
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
    public void verifyByCode() {
        String ip = IpUtil.getIp(request);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try (OutputStream out = response.getOutputStream()) {
            CaptchaUtil captchaUtils = new CaptchaUtil();
            String result = captchaUtils.getCode();
            //ip + code
            // redisUtil.vSet(ip + " code", result, 60, TimeUnit.SECONDS);
            BufferedImage buffImg = captchaUtils.getBuffImg();
            ImageIO.write(buffImg, "png", out);
        } catch (Exception e) {
            log.error("获取验证码失败，error {}", e.getMessage(), e);
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

    public void setRoles(List<UserDTO> userDtos){
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
