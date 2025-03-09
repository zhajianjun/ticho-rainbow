package top.ticho.rainbow.domain.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.dto.request.UserLoginDTO;
import top.ticho.rainbow.application.service.UserService;
import top.ticho.starter.security.dto.LoginRequest;
import top.ticho.starter.security.dto.TiToken;
import top.ticho.starter.security.handle.TiLoginUserHandle;
import top.ticho.starter.view.core.TiSecurityUser;
import top.ticho.starter.web.util.valid.TiValidUtil;

/**
 * @author zhajianjun
 * @date 2024-02-04 11:05
 */
@RequiredArgsConstructor
@Service
public class DefaultLoginUserHandle extends TiLoginUserHandle {

    private final UserService userService;

    @Override
    public TiToken token(LoginRequest loginRequest) {
        if (loginRequest instanceof UserLoginDTO) {
            UserLoginDTO userLogin = (UserLoginDTO) loginRequest;
            TiValidUtil.valid(userLogin);
            userService.imgCodeValid(userLogin.getImgKey(), userLogin.getImgCode());
        }
        String account = loginRequest.getUsername();
        String credentials = loginRequest.getPassword();
        TiSecurityUser baseSecurityUser = this.checkPassword(account, credentials);
        return toToken(baseSecurityUser);
    }

}
