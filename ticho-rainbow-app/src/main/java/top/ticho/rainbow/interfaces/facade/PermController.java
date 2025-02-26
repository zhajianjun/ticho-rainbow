package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.infrastructure.core.component.PermCacheHandle;
import top.ticho.rainbow.interfaces.dto.PermDTO;
import top.ticho.starter.view.core.TiResult;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限标识信息 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("perm")
@Api(tags = "权限标识信息")
@ApiSort(60)
public class PermController {

    @Resource
    private PermCacheHandle permCacheHandle;

    @PreAuthorize("@perm.hasPerms('system:perm:list')")
    @ApiOperation(value = "查询所有权限标识")
    @ApiOperationSupport(order = 60)
    @GetMapping("list")
    public TiResult<List<PermDTO>> listAll() {
        return TiResult.ok(permCacheHandle.listAllAppPerms());
    }

}
