package top.ticho.rainbow.domain.service.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.ticho.boot.security.handle.jwt.JwtExtra;
import top.ticho.rainbow.interfaces.dto.SecurityUser;

import java.util.HashMap;
import java.util.Map;

/**
 * 转换token时需要额外添加的信息
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
