package top.ticho.intranet.server.domain.service.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.ticho.boot.security.handle.jwt.JwtExtra;
import top.ticho.intranet.server.interfaces.dto.SecurityUser;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Component
public class DefaultJwtExtra implements JwtExtra {

    @Override
    public Map<String, Object> getExtra() {
        Map<String, Object> extMap = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof SecurityUser) {
            SecurityUser securityUser = (SecurityUser) principal;
            extMap.put("status", securityUser.getStatus());
        }
        return extMap;
    }

}
