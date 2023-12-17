package top.ticho.intranet.server.infrastructure.core.component;

import com.ticho.boot.security.constant.BaseSecurityConst;
import com.ticho.boot.security.handle.load.LoadUserService;
import com.ticho.boot.view.core.BaseSecurityUser;
import com.ticho.boot.view.enums.HttpErrCode;
import com.ticho.boot.view.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import top.ticho.intranet.server.domain.repository.UserRepository;
import top.ticho.intranet.server.infrastructure.entity.User;

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
    private UserRepository userRepository;

    @Override
    public BaseSecurityUser load(String account) {
        // @formatter:off
        // 用户信息校验
        User user = userRepository.getByUsername(account);
        Assert.isNotNull(user, HttpErrCode.NOT_LOGIN, "用户或者密码不正确");
        BaseSecurityUser securityUser = new BaseSecurityUser();
        securityUser.setUsername(account);
        securityUser.setPassword(user.getPassword());
        securityUser.setRoles(Collections.singletonList("admin"));
        return securityUser;
        // @formatter:on
    }

}
