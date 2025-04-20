package top.ticho.rainbow.application.service.permission;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.security.auth.TiPermissionService;
import top.ticho.starter.view.util.TiAssert;

/**
 * 接口权限实现(文件相关)
 *
 * @author zhajianjun
 * @date 2024-04-27 10:30
 */
@Slf4j
@Service(CommConst.FILE_PERM_KEY)
@Order(1)
public class FilePermissionServiceImpl implements TiPermissionService {

    @Qualifier(CommConst.PERM_KEY)
    @Resource
    private TiPermissionService tiPermissionService;
    @Resource
    private HttpServletRequest request;
    @Resource
    private TiCacheTemplate tiCacheTemplate;

    public boolean hasPerms(String... permissions) {
        log.debug("权限校验，permissions = {}", String.join(",", permissions));
        String chunkId = request.getParameter("chunkId");
        TiAssert.isNotBlank(chunkId, "chunkId is null");
        boolean hasCache = tiCacheTemplate.contain(CacheConst.UPLOAD_CHUNK, chunkId);
        if (hasCache) {
            return true;
        }
        return tiPermissionService.hasPerms(permissions);
    }

}
