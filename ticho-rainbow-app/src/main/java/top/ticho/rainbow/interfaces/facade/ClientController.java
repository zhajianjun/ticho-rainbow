package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.ClientService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.interfaces.command.ClientModifyCommand;
import top.ticho.rainbow.interfaces.command.ClientSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.query.ClientQuery;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @ApiLog("保存客户端")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody ClientSaveCommand clientSaveCommand) {
        clientService.save(clientSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除客户端
     */
    @ApiLog("删除客户端")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        clientService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改客户端
     */
    @ApiLog("修改客户端")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody ClientModifyCommand clientModifyCommand) {
        clientService.modify(clientModifyCommand);
        return TiResult.ok();
    }

    /**
     * 启用客户端
     */
    @ApiLog("启用客户端")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "客户端信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        clientService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用客户端
     */
    @ApiLog("禁用客户端")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "客户端信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        clientService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 查询客户端(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<ClientDTO>> page(@Validated ClientQuery query) {
        return TiResult.ok(clientService.page(query));
    }

    /**
     * 查询所有客户端
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_ALL + "')")
    @GetMapping("all")
    public TiResult<List<ClientDTO>> all() {
        return TiResult.ok(clientService.all());
    }


    /**
     * 导出客户端信息
     *
     * @param query 查询
     * @throws IOException io异常
     */
    @ApiLog("导出客户端信息")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_CLIENT_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated ClientQuery query) throws IOException {
        clientService.exportExcel(query);
    }

}
