package top.ticho.rainbow.domain.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.security.dto.LoginRequest;
import top.ticho.boot.security.dto.Oauth2AccessToken;
import top.ticho.boot.security.handle.BaseLoginUserHandle;
import top.ticho.boot.view.core.BaseSecurityUser;
import top.ticho.boot.view.util.Assert;
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
    private CacheTemplate cacheTemplate;

    @Override
    public Oauth2AccessToken token(LoginRequest loginRequest) {
        if (loginRequest instanceof UserLoginDTO) {
            UserLoginDTO userLogin = (UserLoginDTO) loginRequest;
            String imgCode = userLogin.getImgCode();
            String key = userLogin.getImgKey();
            String cacheImgCode = cacheTemplate.get(CacheConst.VERIFY_CODE, key, String.class);
            Assert.isNotBlank(cacheImgCode, "验证码过期或者不存在");
            cacheTemplate.evict(CacheConst.VERIFY_CODE, key);
            Assert.isTrue(imgCode.equalsIgnoreCase(cacheImgCode), "验证码不正确");
        }
        String account = loginRequest.getUsername();
        String credentials = loginRequest.getPassword();
        BaseSecurityUser baseSecurityUser = this.checkPassword(account, credentials);
        return this.getOauth2TokenAndSetAuthentication(baseSecurityUser);
    }

}
