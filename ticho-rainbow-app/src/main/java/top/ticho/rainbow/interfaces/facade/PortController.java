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
import top.ticho.rainbow.application.dto.command.PortModifyfCommand;
import top.ticho.rainbow.application.dto.command.PortSaveCommand;
import top.ticho.rainbow.application.dto.command.VersionModifyCommand;
import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.application.service.PortService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

/**
 * 端口信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("port")
public class PortController {
    private final PortService portService;

    /**
     * 保存端口
     */
    @ApiLog("保存端口")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody PortSaveCommand portSaveCommand) {
        portService.save(portSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除端口
     */
    @ApiLog("删除端口")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        portService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改端口
     */
    @ApiLog("修改端口")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody PortModifyfCommand portModifyfCommand) {
        portService.modify(portModifyfCommand);
        return TiResult.ok();
    }

    /**
     * 启用端口
     */
    @ApiLog("启用端口")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "端口信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        portService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用端口
     */
    @ApiLog("禁用端口")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "端口信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        portService.disable(datas);
        return TiResult.ok();
    }


    /**
     * 查询端口(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<PortDTO>> page(@Validated PortQuery query) {
        return TiResult.ok(portService.page(query));
    }

    /**
     * 导出端口信息
     */
    @ApiLog("导出端口信息")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.INTRANET_PORT_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated PortQuery query) throws IOException {
        portService.exportExcel(query);
    }

}
