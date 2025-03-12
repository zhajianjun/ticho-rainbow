package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.PortDTO;
import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.service.PortService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import java.io.IOException;

/**
 * 端口信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("port")
public class PortController {
    private final PortService portService;
    /**
     * 查询所有端口(分页)
     *
     * @param query 查询
     * @return {@link TiResult }<{@link TiPageResult }<{@link PortDTO }>>
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:page')")
    @GetMapping
    public TiResult<TiPageResult<PortDTO>> page(@RequestBody PortQuery query) {
        return TiResult.ok(portService.page(query));
    }

    /**
     * 保存端口
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:save')")
    @PostMapping
    public TiResult<Void> save(@RequestBody PortDTO portDTO) {
        portService.save(portDTO);
        return TiResult.ok();
    }

    /**
     * 删除端口
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:remove')")
    @DeleteMapping
    public TiResult<Void> remove(Long id) {
        portService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改端口
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:modify')")
    @PutMapping
    public TiResult<Void> modify(@RequestBody PortDTO portDTO) {
        portService.modify(portDTO);
        return TiResult.ok();
    }

    /**
     * 查询端口
     *
     * @param id 编号
     * @return {@link TiResult }<{@link PortDTO }>
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:getById')")
    @GetMapping("{id}")
    public TiResult<PortDTO> getById(@PathVariable Long id) {
        return TiResult.ok(portService.getById(id));
    }


    /**
     * 导出端口信息
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('intranet:port:expExcel')")
    @PostMapping("expExcel")
    public void expExcel(@RequestBody PortQuery query) throws IOException {
        portService.expExcel(query);
    }

}
