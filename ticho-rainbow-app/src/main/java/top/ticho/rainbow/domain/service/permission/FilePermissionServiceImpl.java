package top.ticho.rainbow.domain.service.permission;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import top.ticho.boot.security.auth.PermissionService;
import top.ticho.boot.view.util.Assert;
import top.ticho.rainbow.infrastructure.core.component.cache.SpringCacheTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.constant.CommConst;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口权限实现(文件相关)
 *
 * @author zhajianjun
 * @date 2024-04-27 10:30
 */
@Slf4j
@Service(CommConst.FILE_PERM_KEY)
@Order(1)
public class FilePermissionServiceImpl implements PermissionService {

    @Resource
    private HttpServletRequest request;

    @Autowired
    private SpringCacheTemplate cacheTemplate;

    @Autowired
    @Qualifier("perm")
    private PermissionService permissionService;

    public boolean hasPerms(String... permissions) {
        log.debug("权限校验，permissions = {}", String.join(",", permissions));
        String chunkId = request.getParameter("chunkId");
        Assert.isNotBlank(chunkId, "chunkId is null");
        boolean hasCache = cacheTemplate.contain(CacheConst.UPLOAD_CHUNK, chunkId);
        if (hasCache) {
            return true;
        }
        return permissionService.hasPerms(permissions);
    }

}
