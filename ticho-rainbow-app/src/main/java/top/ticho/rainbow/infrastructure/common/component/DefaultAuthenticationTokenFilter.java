package top.ticho.rainbow.infrastructure.common.component;

import org.springframework.stereotype.Component;
import top.ticho.rainbow.infrastructure.common.dto.SecurityUser;
import top.ticho.starter.security.constant.TiSecurityConst;
import top.ticho.starter.security.filter.AbstractAuthTokenFilter;
import top.ticho.tool.core.TiNumberUtil;
import top.ticho.tool.json.util.TiJsonUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * jwt token验证过滤器
 *
 * @author zhajianjun
 * @date 2022-09-24 14:14:06
 */
@Component
public class DefaultAuthenticationTokenFilter extends AbstractAuthTokenFilter<SecurityUser> {

    @Override
    public SecurityUser convert(Map<String, Object> decodeAndVerify) {
        String username = Optional.ofNullable(decodeAndVerify.get(TiSecurityConst.USERNAME))
            .map(Object::toString)
            .orElse(null);
        List<String> authorities = Optional.ofNullable(decodeAndVerify.get(TiSecurityConst.AUTHORITIES))
            .map(x -> TiJsonUtil.toList(TiJsonUtil.toJsonString(x), String.class))
            .orElse(null);
        Integer status = Optional.ofNullable(decodeAndVerify.get("status"))
            .map(Object::toString)
            .map(TiNumberUtil::parseInt)
            .orElse(null);
        SecurityUser user = new SecurityUser();
        user.setUsername(username);
        user.setRoles(authorities);
        user.setStatus(status);
        return user;
    }

}
