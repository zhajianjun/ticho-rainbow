package top.ticho.rainbow.application.service.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.executor.AuthExecutor;
import top.ticho.rainbow.application.executor.UserExecutor;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.infrastructure.common.constant.SecurityConst;
import top.ticho.rainbow.infrastructure.common.dto.SecurityUser;
import top.ticho.rainbow.infrastructure.common.enums.UserStatus;
import top.ticho.rainbow.infrastructure.common.util.UserUtil;
import top.ticho.rainbow.interfaces.dto.UserDTO;
import top.ticho.starter.security.auth.TiPermissionService;
import top.ticho.starter.security.constant.TiSecurityConst;

import jakarta.servlet.http.HttpServletRequest;
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
public class DefaultPermissionService implements TiPermissionService {

    private final HttpServletRequest request;
    private final AuthExecutor authExecutor;
    private final UserExecutor userExecutor;

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
        UserDTO user = userExecutor.find(currentUser.getUsername());
        if (Objects.isNull(user) || !Objects.equals(user.getStatus(), UserStatus.NORMAL.code())) {
            return false;
        }
        List<String> perms = authExecutor.getPerms(roleCodes);
        if (CollUtil.isEmpty(perms)) {
            return false;
        }
        return Arrays.stream(permissions).anyMatch(perms::contains);
    }

}
