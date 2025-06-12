package top.ticho.rainbow.application.executor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.application.assembler.UserAssembler;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.interfaces.dto.response.UserDTO;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.util.TiAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2025-04-06 19:37
 */
@Component
@RequiredArgsConstructor
public class UserExecutor {
    private final TiCacheTemplate tiCacheTemplate;
    private final UserAssembler userAssembler;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public void imgCodeValid(String key, String imgCode) {
        String cacheImgCode = tiCacheTemplate.get(CacheConst.VERIFY_CODE, key, String.class);
        TiAssert.isNotBlank(cacheImgCode, "验证码已过期");
        tiCacheTemplate.evict(CacheConst.VERIFY_CODE, key);
        TiAssert.isTrue(imgCode.equalsIgnoreCase(cacheImgCode), "验证码不正确");
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void modifyPassword(String password, String newPassword, User user) {
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        TiAssert.isTrue(matches, "密码错误");
        String encodedPasswordNew = passwordEncoder.encode(newPassword);
        user.modifyPassword(encodedPasswordNew);
        TiAssert.isTrue(userRepository.modify(user), "更新密码失败");
    }

    public UserDTO find(String username) {
        User user = userRepository.findCacheByUsername(username);
        return userAssembler.toDTO(user);
    }

    public void setRoles(List<UserDTO> userDtos) {
        if (CollUtil.isEmpty(userDtos)) {
            return;
        }
        Map<Long, List<Long>> userRoleIdsMap = userDtos
            .stream()
            .collect(Collectors.toMap(UserDTO::getId, x -> userRoleRepository.listByUserId(x.getId())));
        for (UserDTO userDto : userDtos) {
            Long id = userDto.getId();
            List<Long> itemRoleIds = Optional.ofNullable(userRoleIdsMap.get(id))
                .orElseGet(ArrayList::new);
            userDto.setRoleIds(itemRoleIds);
        }
    }

    /**
     * 保存或者修改用户信息重复数据判断，用户名称、邮箱、手机号保证其唯一性
     */
    public List<String> checkRepeat(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String mobile = user.getMobile();
        List<User> users = userRepository.findByAccount(username, email, mobile);
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

}
