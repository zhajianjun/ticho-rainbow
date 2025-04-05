package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.command.PortModifyfCommand;
import top.ticho.rainbow.application.dto.command.PortSaveCommand;
import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.application.service.PortService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.validation.constraints.NotNull;
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
     * 保存端口
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody PortSaveCommand portSaveCommand) {
        portService.save(portSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除端口
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        portService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改端口
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody PortModifyfCommand portModifyfCommand) {
        portService.modify(portModifyfCommand);
        return TiResult.ok();
    }

    /**
     * 查询端口
     *
     * @param id 编号
     * @return {@link TiResult }<{@link PortDTO }>
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:find')")
    @GetMapping
    public TiResult<PortDTO> find(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(portService.find(id));
    }


    /**
     * 查询端口(分页)
     */
    @PreAuthorize("@perm.hasPerms('intranet:port:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<PortDTO>> page(@Validated PortQuery query) {
        return TiResult.ok(portService.page(query));
    }

    /**
     * 导出端口信息
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('intranet:port:export')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated PortQuery query) throws IOException {
        portService.exportExcel(query);
    }

}
