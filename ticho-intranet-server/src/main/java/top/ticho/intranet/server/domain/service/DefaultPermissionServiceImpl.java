package top.ticho.intranet.server.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import top.ticho.boot.security.auth.PermissionService;
import top.ticho.boot.security.constant.BaseOAuth2Const;
import top.ticho.intranet.server.domain.handle.UpmsHandle;
import top.ticho.intranet.server.infrastructure.core.constant.CommConst;
import top.ticho.intranet.server.infrastructure.core.constant.SecurityConst;
import top.ticho.intranet.server.infrastructure.core.util.UserUtil;
import top.ticho.intranet.server.interfaces.dto.SecurityUser;

import javax.annotation.Resource;
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
@Service(CommConst.PERM_KEY)
@Order(1)
public class DefaultPermissionServiceImpl extends UpmsHandle implements PermissionService {

    @Resource
    private HttpServletRequest request;

    public boolean hasPerms(String... permissions) {
        log.debug("权限校验，permissions = {}", String.join(",", permissions));
        boolean inner = Objects.equals(request.getHeader(BaseOAuth2Const.INNER), BaseOAuth2Const.INNER_VALUE);
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
        List<String> perms = getPerms(roleCodes);
        if (CollUtil.isEmpty(perms)) {
            return false;
        }
        return Arrays.stream(permissions).anyMatch(perms::contains);
    }

}
