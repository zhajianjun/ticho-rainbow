package top.ticho.rainbow.domain.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.security.dto.LoginRequest;
import top.ticho.boot.security.dto.Oauth2AccessToken;
import top.ticho.boot.security.handle.BaseLoginUserHandle;
import top.ticho.boot.view.core.BaseSecurityUser;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.rainbow.infrastructure.core.component.CacheTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;

/**
 * @author zhajianjun
 * @date 2024-02-04 11:05
 */
@Service
public class DefaultLoginUserHandle extends BaseLoginUserHandle {

    @Autowired
    private UserService userService;

    @Override
    public Oauth2AccessToken token(LoginRequest loginRequest) {
        if (loginRequest instanceof UserLoginDTO) {
            UserLoginDTO userLogin = (UserLoginDTO) loginRequest;
            ValidUtil.valid(userLogin);
            userService.imgCodeValid(userLogin);
        }
        String account = loginRequest.getUsername();
        String credentials = loginRequest.getPassword();
        BaseSecurityUser baseSecurityUser = this.checkPassword(account, credentials);
        return this.getOauth2TokenAndSetAuthentication(baseSecurityUser);
    }

}
