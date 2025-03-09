package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.PermDTO;
import top.ticho.rainbow.infrastructure.core.component.PermCacheHandle;
import top.ticho.starter.view.core.TiResult;

import java.util.List;

/**
 * 权限标识信息
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("perm")
public class PermController {
    private final PermCacheHandle permCacheHandle;

    /**
     * 查询所有权限标识
     *
     * @return {@link TiResult }<{@link List }<{@link PermDTO }>>
     */
    @PreAuthorize("@perm.hasPerms('system:perm:all')")
    @GetMapping("list")
    public TiResult<List<PermDTO>> listAll() {
        return TiResult.ok(permCacheHandle.listAllAppPerms());
    }

}
