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
import top.ticho.rainbow.application.service.DictService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.interfaces.command.DictModifyCommand;
import top.ticho.rainbow.interfaces.command.DictSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.query.DictQuery;
import top.ticho.rainbow.interfaces.dto.DictCacheDTO;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

/**
 * 字典
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("dict")
public class DictController {
    private final DictService dictService;

    /**
     * 保存字典
     */
    @ApiLog("保存字典")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody DictSaveCommand dictSaveCommand) {
        dictService.save(dictSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除字典
     */
    @ApiLog("删除字典")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        dictService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改字典
     */
    @ApiLog("修改字典")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody DictModifyCommand dictModifyCommand) {
        dictService.modify(dictModifyCommand);
        return TiResult.ok();
    }

    /**
     * 启用字典
     */
    @ApiLog("启用字典")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "字典信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        dictService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用字典
     */
    @ApiLog("禁用字典")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "字典信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        dictService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 查询字典(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<DictDTO>> page(@Validated DictQuery query) {
        return TiResult.ok(dictService.page(query));
    }

    /**
     * 查询所有有效字典
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_ALL + "')")
    @GetMapping("all")
    public TiResult<List<DictCacheDTO>> list() {
        return TiResult.ok(dictService.list());
    }

    /**
     * 刷新所有有效字典
     */
    @ApiLog("刷新所有有效字典")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_FLUSH + "')")
    @GetMapping("flush")
    public TiResult<List<DictCacheDTO>> flush() {
        return TiResult.ok(dictService.flush());
    }

    /**
     * 导出字典
     */
    @ApiLog("导出字典")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated DictQuery query) throws IOException {
        dictService.exportExcel(query);
    }

}
