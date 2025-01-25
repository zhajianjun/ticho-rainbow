package top.ticho.rainbow.domain.service.login;

import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.system.service.UserService;
import top.ticho.rainbow.interfaces.dto.UserLoginDTO;
import top.ticho.starter.security.dto.LoginRequest;
import top.ticho.starter.security.dto.Oauth2AccessToken;
import top.ticho.starter.security.handle.BaseLoginUserHandle;
import top.ticho.starter.view.core.TiSecurityUser;
import top.ticho.starter.web.util.valid.TiValidUtil;

import javax.annotation.Resource;

/**
 * @author zhajianjun
 * @date 2024-02-04 11:05
 */
@Service
public class DefaultLoginUserHandle extends BaseLoginUserHandle {

    @Resource
    private UserService userService;

    @Override
    public Oauth2AccessToken token(LoginRequest loginRequest) {
        if (loginRequest instanceof UserLoginDTO) {
            UserLoginDTO userLogin = (UserLoginDTO) loginRequest;
            TiValidUtil.valid(userLogin);
            userService.imgCodeValid(userLogin);
        }
        String account = loginRequest.getUsername();
        String credentials = loginRequest.getPassword();
        TiSecurityUser baseSecurityUser = this.checkPassword(account, credentials);
        return this.getOauth2TokenAndSetAuthentication(baseSecurityUser);
    }

}
