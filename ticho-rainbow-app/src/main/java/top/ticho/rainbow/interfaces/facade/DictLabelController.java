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
import top.ticho.rainbow.application.service.DictLabelService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.interfaces.command.DictLabelModifyCommand;
import top.ticho.rainbow.interfaces.command.DictLabelSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;
import top.ticho.starter.view.core.TiResult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 字典标签
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("dict-label")
public class DictLabelController {
    private final DictLabelService dictLabelService;

    /**
     * 保存字典标签
     */
    @ApiLog("保存字典标签")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_LABEL_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody DictLabelSaveCommand dictLabelSaveCommand) {
        dictLabelService.save(dictLabelSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除字典标签
     */
    @ApiLog("删除字典标签")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_LABEL_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        dictLabelService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改字典标签
     */
    @ApiLog("修改字典标签")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_LABEL_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody DictLabelModifyCommand dictLabelModifyCommand) {
        dictLabelService.modify(dictLabelModifyCommand);
        return TiResult.ok();
    }

    /**
     * 启用字典标签
     */
    @ApiLog("启用字典标签")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_LABEL_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "字典标签信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        dictLabelService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 禁用字典标签
     */
    @ApiLog("禁用字典标签")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_LABEL_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "字典标签信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        dictLabelService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 根据编码查询字典标签
     *
     * @param code 字典编码
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_DICT_LABEL_FIND + "')")
    @GetMapping
    public TiResult<List<DictLabelDTO>> find(@NotBlank(message = "字典编码不能为空") String code) {
        return TiResult.ok(dictLabelService.find(code));
    }

}
