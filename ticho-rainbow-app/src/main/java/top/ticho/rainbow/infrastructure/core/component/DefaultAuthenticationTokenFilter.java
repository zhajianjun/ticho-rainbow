package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.convert.Convert;
import org.springframework.stereotype.Component;
import top.ticho.rainbow.interfaces.dto.SecurityUser;
import top.ticho.starter.security.constant.BaseOAuth2Const;
import top.ticho.starter.security.constant.BaseSecurityConst;
import top.ticho.starter.security.filter.AbstractAuthTokenFilter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * jwt token验证过滤器
 *
 * @author zhajianjun
 * @date 2022-09-24 14:14:06
 */
@Component(BaseOAuth2Const.OAUTH2_TOKEN_FILTER_BEAN_NAME)
public class DefaultAuthenticationTokenFilter extends AbstractAuthTokenFilter<SecurityUser> {

    @Override
    public SecurityUser convert(Map<String, Object> decodeAndVerify) {
        String username = Optional.ofNullable(decodeAndVerify.get(BaseSecurityConst.USERNAME))
            .map(Object::toString)
            .orElse(null);
        List<String> authorities = Optional.ofNullable(decodeAndVerify.get(BaseSecurityConst.AUTHORITIES)).map(x -> Convert.toList(String.class, x)).orElse(null);
        Integer status = Optional.ofNullable(decodeAndVerify.get("status"))
            .map(Convert::toInt)
            .orElse(null);
        SecurityUser user = new SecurityUser();
        user.setUsername(username);
        user.setRoles(authorities);
        user.setStatus(status);
        return user;
    }

}
