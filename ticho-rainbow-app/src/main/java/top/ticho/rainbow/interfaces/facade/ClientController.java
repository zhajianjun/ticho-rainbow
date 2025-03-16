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
import top.ticho.rainbow.application.dto.command.ClientModifyCommand;
import top.ticho.rainbow.application.dto.command.ClientSaveCommand;
import top.ticho.rainbow.application.dto.query.ClientQuery;
import top.ticho.rainbow.application.dto.response.ClientDTO;
import top.ticho.rainbow.application.service.ClientService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 客户端信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("client")
public class ClientController {
    private final ClientService clientService;

    /**
     * 保存客户端
     */
    @PreAuthorize("@perm.hasPerms('intranet:client:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody ClientSaveCommand clientSaveCommand) {
        clientService.save(clientSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除客户端
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('intranet:client:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        clientService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改客户端
     */
    @PreAuthorize("@perm.hasPerms('intranet:client:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody ClientModifyCommand clientModifyCommand) {
        clientService.modify(clientModifyCommand);
        return TiResult.ok();
    }

    /**
     * 查询客户端
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('intranet:client:getById')")
    @GetMapping
    public TiResult<ClientDTO> getById(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(clientService.getById(id));
    }

    /**
     * 查询客户端(分页)
     */
    @PreAuthorize("@perm.hasPerms('intranet:client:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<ClientDTO>> page(@Validated ClientQuery query) {
        return TiResult.ok(clientService.page(query));
    }

    /**
     * 查询所有客户端
     */
    @PreAuthorize("@perm.hasPerms('intranet:client:all')")
    @GetMapping("all")
    public TiResult<List<ClientDTO>> list(@Validated ClientQuery query) {
        return TiResult.ok(clientService.list(query));
    }


    /**
     * 导出客户端信息
     *
     * @param query 查询
     * @throws IOException io异常
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('intranet:client:exportExcel')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated ClientQuery query) throws IOException {
        clientService.exportExcel(query);
    }

}
