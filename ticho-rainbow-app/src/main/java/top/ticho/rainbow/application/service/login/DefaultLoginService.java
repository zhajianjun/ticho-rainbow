package top.ticho.rainbow.application.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.request.UserLoginDTO;
import top.ticho.rainbow.application.executor.UserExecutor;
import top.ticho.rainbow.domain.entity.Role;
import top.ticho.rainbow.domain.entity.User;
import top.ticho.rainbow.domain.repository.RoleRepository;
import top.ticho.rainbow.domain.repository.UserRepository;
import top.ticho.rainbow.domain.repository.UserRoleRepository;
import top.ticho.rainbow.infrastructure.common.enums.UserStatus;
import top.ticho.starter.security.dto.TiToken;
import top.ticho.starter.security.service.impl.AbstractLoginService;
import top.ticho.starter.view.core.TiSecurityUser;
import top.ticho.starter.view.enums.TiHttpErrCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.valid.TiValidUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhajianjun
 * @date 2024-02-04 11:05
 */
@RequiredArgsConstructor
@Service
public class DefaultLoginService extends AbstractLoginService {
    private final UserExecutor userExecutor;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

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

    public TiToken token(UserLoginDTO userLogin) {
        TiValidUtil.valid(userLogin);
        userExecutor.imgCodeValid(userLogin.getImgKey(), userLogin.getImgCode());
        String account = userLogin.getUsername();
        String credentials = userLogin.getPassword();
        TiSecurityUser baseSecurityUser = this.checkPassword(account, credentials);
        return toToken(baseSecurityUser);
    }

}
