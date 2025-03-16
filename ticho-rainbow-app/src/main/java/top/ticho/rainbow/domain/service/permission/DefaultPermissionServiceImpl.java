package top.ticho.rainbow.domain.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.SecurityUser;
import top.ticho.rainbow.application.dto.UserDTO;
import top.ticho.rainbow.application.service.AbstractAuthServiceImpl;
import top.ticho.rainbow.infrastructure.core.constant.CommConst;
import top.ticho.rainbow.infrastructure.core.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.core.enums.UserStatus;
import top.ticho.rainbow.infrastructure.core.util.UserUtil;
import top.ticho.starter.security.auth.PermissionService;
import top.ticho.starter.security.constant.TiSecurityConst;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 接口权限实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Slf4j
@RequiredArgsConstructor
@Service(CommConst.PERM_KEY)
@Order(1)
public class DefaultPermissionServiceImpl extends AbstractAuthServiceImpl implements PermissionService {

    private final HttpServletRequest request;

    public boolean hasPerms(String... permissions) {
        log.debug("权限校验，permissions = {}", String.join(",", permissions));
        boolean inner = Objects.equals(request.getHeader(TiSecurityConst.INNER), TiSecurityConst.INNER_VALUE);
        if (inner) {
            return true;
        }
        if (ArrayUtil.isEmpty(permissions)) {
            return false;
        }
        SecurityUser currentUser = UserUtil.getCurrentUser();
        if (Objects.isNull(currentUser)) {
            return false;
        }
        List<String> roleCodes = currentUser.getRoles();
        if (CollUtil.isEmpty(roleCodes)) {
            return false;
        }
        if (roleCodes.contains(SecurityConst.ADMIN)) {
            return true;
        }
        UserDTO user = getUser(currentUser.getUsername());
        if (Objects.isNull(user) || !Objects.equals(user.getStatus(), UserStatus.NORMAL.code())) {
            return false;
        }
        List<String> perms = getPerms(roleCodes);
        if (CollUtil.isEmpty(perms)) {
            return false;
        }
        return Arrays.stream(permissions).anyMatch(perms::contains);
    }

}
