package top.ticho.intranet.server.infrastructure.core.component;

import com.ticho.boot.security.constant.BaseSecurityConst;
import com.ticho.boot.security.handle.load.LoadUserService;
import com.ticho.boot.view.core.BaseSecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 默认的登录用户查询服务
 *
 * @author zhajianjun
 * @date 2023-12-17 08:30
 */
@Component(BaseSecurityConst.LOAD_USER_TYPE_USERNAME)
@Primary
@Slf4j
public class DefaultUsernameLoadUserService implements LoadUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public BaseSecurityUser load(String account) {
        // @formatter:off
        // 用户信息校验
        BaseSecurityUser securityUser = new BaseSecurityUser();
        securityUser.setUsername(account);
        securityUser.setPassword(passwordEncoder.encode("123456"));
        securityUser.setRoles(Collections.singletonList("admin"));
        return securityUser;
        // @formatter:on
    }

}
