package top.ticho.rainbow.domain.service.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.core.enums.UserStatus;
import top.ticho.rainbow.infrastructure.entity.Role;
import top.ticho.rainbow.infrastructure.entity.User;
import top.ticho.rainbow.interfaces.dto.SecurityUser;
import top.ticho.starter.security.constant.BaseSecurityConst;
import top.ticho.starter.security.handle.load.LoadUserService;
import top.ticho.starter.view.enums.TiHttpErrCode;
import top.ticho.starter.view.util.TiAssert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Component(BaseSecurityConst.LOAD_USER_TYPE_USERNAME)
@Primary
@Slf4j
public class DefaultUsernameLoadUserService implements LoadUserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserRoleRepository userRoleRepository;

    @Resource
    private RoleRepository roleRepository;

    @Override
    public SecurityUser load(String account) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        // 用户信息校验
        User user = userRepository.getCacheByUsername(account);
        TiAssert.isNotNull(user, TiHttpErrCode.NOT_LOGIN, "用户或者密码不正确");
        Integer status = user.getStatus();
        String message = UserStatus.getByCode(status);
        boolean normal = Objects.equals(status, UserStatus.NORMAL.code());
        TiAssert.isTrue(normal, TiHttpErrCode.NOT_LOGIN, String.format("用户%s", message));
        return getSecurityUser(user);
    }

    private SecurityUser getSecurityUser(User user) {
        List<Long> roleIds = userRoleRepository.listByUserId(user.getId());
        List<Role> roles = roleRepository.listByIds(roleIds);
        List<String> codes = roles.stream().filter(x -> Objects.equals(1, x.getStatus())).map(Role::getCode).collect(Collectors.toList());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUsername(user.getUsername());
        securityUser.setPassword(user.getPassword());
        securityUser.setRoles(codes);
        securityUser.setStatus(user.getStatus());
        return securityUser;
    }

}
